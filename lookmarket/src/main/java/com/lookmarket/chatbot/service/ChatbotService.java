// com.lookmarket.chatbot.service.ChatbotService
package com.lookmarket.chatbot.service;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Semaphore;
import javax.net.ssl.SSLHandshakeException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service("chatbotService")
public class ChatbotService {
    private final WebClient openAIClient;

    // ✅ properties에서 직접 주입해서, 요청마다 Bearer 헤더를 강제 추가
    private final String apiKey;

    // ✅ 동시 요청 제한/재시도 정책은 그대로
    private final Semaphore limiter = new Semaphore(2, true);
    private final int maxAttempts = 4;
    private final long baseDelayMs = 1200L;
    private final double jitterMin = 0.5;
    private final double jitterMax = 1.5;

    public ChatbotService(
            @Qualifier("openAIClient") WebClient openAIClient,
            @Value("${OPENAI_API_KEY:${openai.api-key:}}") String apiKey
    ) {
        this.openAIClient = openAIClient;
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        // 간단한 가드 (서버 부팅 직후 콘솔 체크용)
        if (this.apiKey.isEmpty()) {
            System.err.println("[OpenAI] WARNING: openai.api-key 가 비어 있습니다.");
        } else {
            System.out.println("[OpenAI] apiKey prefix = " + maskKey(this.apiKey));
        }
    }

    @SuppressWarnings("unchecked")
    public String getChatbotResponse(String userMessage) {
        try {
            limiter.acquire();
            int attempt = 0;

            while (true) {
                attempt++;
                try {
                    Map<String, Object> request = new HashMap<>();
                    request.put("model", "gpt-4o-mini");
                    List<Map<String, String>> messages = new ArrayList<>();
                    messages.add(Map.of("role", "system", "content", "당신은 친절한 고객센터 챗봇입니다."));
                    messages.add(Map.of("role", "user", "content", userMessage));
                    request.put("messages", messages);
                    request.put("temperature", 0.7);
                    request.put("max_tokens", 300);

                    Map<String, Object> resp = openAIClient.post()
                        .uri("/chat/completions")
                        // ✅ 매 요청마다 Bearer 헤더를 강제
                        .headers(h -> {
                            if (apiKey.isEmpty()) return; // 비어있으면 어차피 401
                            // setBearerAuth 가 자동으로 "Bearer ..." 세팅
                            h.setBearerAuth(apiKey);
                            // 진단용: Authorization 존재 여부만 콘솔에
                            if (!h.containsKey(HttpHeaders.AUTHORIZATION)) {
                                System.err.println("[OpenAI] Authorization header not set!");
                            }
                        })
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .timeout(Duration.ofSeconds(35))
                        .block();

                    if (resp == null) return "응답이 비어 있습니다.";
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
                    if (choices == null || choices.isEmpty()) return "응답 선택지가 없습니다.";
                    Map<String, Object> first = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) first.get("message");
                    if (message == null) return "응답 메시지를 찾지 못했습니다.";
                    Object content = message.get("content");
                    return (content == null) ? "내용이 없습니다." : content.toString();

                } catch (WebClientResponseException e) {
                    int sc = e.getRawStatusCode();
                    String body = safeBody(e);

                    if (sc == 401) {
                        // ✅ 키/헤더 진단 메시지 강화
                        String hint;
                        if (apiKey.isEmpty()) {
                            hint = "인증 실패(401): openai.api-key 값이 비어 있습니다. application.properties 환경설정 확인!";
                        } else {
                            hint = "인증 실패(401): API 키가 잘못되었거나 권한이 없습니다. (키 앞자리 "
                                   + maskKey(apiKey) + ", Authorization 헤더 강제 세팅함)";
                        }
                        return hint;
                    }

                    if (sc == 429 || sc == 503) {
                        if (body != null && body.contains("insufficient_quota")) {
                            return "한도/크레딧(Quota) 초과입니다. 충전 후 다시 시도하세요.";
                        }
                        long retryAfterMs = parseRetryAfterMs(e.getHeaders());
                        long exponential = (long) (baseDelayMs * Math.pow(2, attempt - 1));
                        long jittered = (long) (exponential * rnd(jitterMin, jitterMax));
                        long sleepMs = Math.max(retryAfterMs, jittered);
                        if (attempt < maxAttempts) { sleepQuietly(sleepMs); continue; }
                        return "요청이 많아 잠시 지연되고 있습니다(429/503). 잠시 후 다시 시도해주세요.";
                    }

                    return "오류(" + sc + "): " + (body != null ? body : e.getMessage());

                } catch (WebClientRequestException e) {
                    Throwable root = rootCause(e);
                    String hint = "";

                    if (root instanceof UnknownHostException || containsNameResolverKeyword(root)) {
                        hint = "DNS 해석 실패: 학원망/프록시 설정 또는 DNS 차단 가능성이 큽니다.";
                        logNet("DNS", e);
                        return hint + " (api.openai.com)";
                    }
                    if (root instanceof ConnectException || messageContains(e, "Connection timed out")) {
                        hint = "연결 타임아웃: 프록시 미설정/방화벽 차단/네트워크 지연 가능성.";
                        logNet("CONNECT", e);
                        return hint;
                    }
                    if (root instanceof SSLHandshakeException || messageContains(e, "handshake_failure")) {
                        hint = "SSL 핸드셰이크 실패: SSL 검사/프록시 가짜 인증서 문제 가능.";
                        logNet("SSL", e);
                        return hint;
                    }
                    if (messageContains(e, "proxy") || messageContains(e, "ProxyConnectException")) {
                        hint = "프록시 연결 실패: 프록시 주소/포트/인증 필요 여부 확인.";
                        logNet("PROXY", e);
                        return hint;
                    }

                    logNet("NETWORK", e);
                    return "네트워크 오류: " + root.getClass().getSimpleName() + " - " + root.getMessage();

                } catch (Exception ex) {
                    logNet("UNEXPECTED", ex);
                    return "오류: " + ex.getMessage();
                }
            }

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return "요청이 취소되었습니다.";
        } finally {
            limiter.release();
        }
    }

    // ---------- helpers ----------
    private static String safeBody(WebClientResponseException e) {
        try { return e.getResponseBodyAsString(); } catch (Exception ignore) { return null; }
    }
    private static void sleepQuietly(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }
    private static boolean messageContains(Throwable t, String kw) {
        String m = String.valueOf(t.getMessage());
        return m != null && m.toLowerCase().contains(kw.toLowerCase());
    }
    private static boolean containsNameResolverKeyword(Throwable root) {
        String s = (root.getClass().getName() + " " + String.valueOf(root.getMessage())).toLowerCase();
        return s.contains("nameresolver") || s.contains("dnserror") || s.contains("resolve");
    }
    private static Throwable rootCause(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null && cur.getCause() != cur) cur = cur.getCause();
        return cur;
    }
    private static void logNet(String tag, Throwable t) {
        System.err.println("[NET][" + tag + "] " + t.getClass().getName() + ": " + t.getMessage());
        t.printStackTrace(System.err);
    }
    private static long parseRetryAfterMs(org.springframework.http.HttpHeaders headers) {
        try {
            String ra = headers.getFirst("Retry-After");
            if (ra == null) return 0L;
            return Long.parseLong(ra.trim()) * 1000L;
        } catch (Exception ignore) { return 0L; }
    }
    private static double rnd(double min, double max) {
        return min + (Math.random() * (max - min));
    }
    private static String maskKey(String key) {
        if (key == null || key.isEmpty()) return "(empty)";
        // 예: sk-abc123... 형태 → 앞 7글자만 보이게
        int end = Math.min(key.length(), 7);
        return key.substring(0, end) + "...";
    }
}
