// com.lookmarket.chatbot.service.ChatbotService
package com.lookmarket.chatbot.service;

import java.util.*;
import java.util.concurrent.Semaphore;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.stereotype.Service;

/**
 * 429/503(서버 과부하/레이트리밋) 자동 재시도 + 동시성  제한을 걸어
 * "보내자마자 429"를 줄인다.
 */
@Service("chatbotService")
public class ChatbotService {
    private final WebClient openAIClient;

    // ✅ 동시 요청 개수 제한 (JVM 단위). 너무 많이 때리면 429 난다.
    private final Semaphore limiter = new Semaphore(2, true); // 필요 시 properties로 뺄 것

    // ✅ 재시도 정책 (지수 백오프 + 지터)
    private final int maxAttempts = 4;        // 총 4번까지 시도
    private final long baseDelayMs = 1200L;   // 1.2초 시작 → 2.4s → 4.8s …
    private final double jitterMin = 0.5;     // 50% ~ 150% 지터
    private final double jitterMax = 1.5;

    public ChatbotService(WebClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    @SuppressWarnings("unchecked")
    public String getChatbotResponse(String userMessage) {
        try {
            // 1) 동시성 제한: 동시에 많은 스레드가 때리는 걸 막는다.
            limiter.acquire();

            int attempt = 0;
            while (true) {
                attempt++;
                try {
                    // 2) OpenAI Chat Completions 요청 바디 (토큰 상한을 적당히 제한해 TPM 압박↓)
                    Map<String, Object> request = new HashMap<>();
                    request.put("model", "gpt-4o-mini");
                    List<Map<String, String>> messages = new ArrayList<>();
                    messages.add(Map.of("role", "system", "content", "당신은 친절한 고객센터 챗봇입니다."));
                    messages.add(Map.of("role", "user", "content", userMessage));
                    request.put("messages", messages);
                    request.put("temperature", 0.7);
                    request.put("max_tokens", 300); // ✅ 응답 길이 상한 → 토큰 사용량/속도 제한

                    Map<String, Object> resp = openAIClient.post()
                        .uri("/chat/completions")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block(); // MVC라 동기 처리

                    // 3) 정상 파싱
                    if (resp == null) return "응답이 비어 있습니다.";
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
                    if (choices == null || choices.isEmpty()) return "응답 선택지가 없습니다.";
                    Map<String, Object> first = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) first.get("message");
                    if (message == null) return "응답 메시지를 찾지 못했습니다.";
                    Object content = message.get("content");
                    return (content == null) ? "내용이 없습니다." : content.toString();

                } catch (WebClientResponseException e) {
                    // 4) 상태코드별 처리
                    int sc = e.getRawStatusCode();
                    String body = safeBody(e);

                    if (sc == 401) {
                        return "인증 실패(401): API 키/환경변수를 확인해주세요.";
                    }
                    if (sc == 429 || sc == 503) {
                        // ✅ 레이트리밋/서버바쁨 → 재시도
                        //    429라도 'insufficient_quota'면 돈/크레딧 문제라 재시도해도 소용없음.
                        if (body != null && body.contains("insufficient_quota")) {
                            return "한도/크레딧(Quota) 초과입니다. 결제/크레딧을 충전한 뒤 다시 시도하세요.";
                        }
                        // 서버가 Retry-After를 주면 최우선 반영
                        long retryAfterMs = parseRetryAfterMs(e.getHeaders());
                        // 지수 백오프 + 지터
                        long exponential = (long) (baseDelayMs * Math.pow(2, attempt - 1));
                        long jittered = (long) (exponential * rnd(jitterMin, jitterMax));
                        long sleepMs = Math.max(retryAfterMs, jittered);

                        if (attempt < maxAttempts) {
                            try { Thread.sleep(sleepMs); } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                return "요청이 취소되었습니다.";
                            }
                            continue; // 다음 시도
                        }
                        // 재시도 소진 → 사용자에게 안내
                        return "요청이 많아 잠시 지연되고 있습니다(429). 잠시 후 다시 시도해주세요.";

                    } else if (sc >= 500) {
                        // 5xx 는 일시 서버 오류 → 한 번만 더 시도해도 됨(여기선 백오프 로직과 통합할 수 있음)
                        if (attempt < maxAttempts) {
                            try { Thread.sleep((long)(baseDelayMs * rnd(jitterMin, jitterMax))); } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                return "요청이 취소되었습니다.";
                            }
                            continue;
                        }
                        return "서버 오류(" + sc + "): 잠시 후 다시 시도해주세요.";

                    } else {
                        // 4xx 기타 오류: 메시지 표출
                        return "오류(" + sc + "): " + (body != null ? body : e.getMessage());
                    }

                } catch (Exception ex) {
                    // 예기치 못한 오류는 메시지로 노출
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

    private static String safeBody(WebClientResponseException e) {
        try { return e.getResponseBodyAsString(); } catch (Exception ignore) { return null; }
    }

    /**
     * Retry-After 헤더(ms) 파싱. 숫자면 초로 간주하여 변환.
     * (HTTP-date 포맷까지 파싱하려면 Date 헤더 비교가 필요하지만, 보통 숫자로 온다)
     */
    private static long parseRetryAfterMs(HttpHeaders headers) {
        if (headers == null) return 0L;
        String ra = headers.getFirst("Retry-After");
        if (ra == null) return 0L;
        try { return Long.parseLong(ra.trim()) * 1000L; } catch (NumberFormatException e) { return 0L; }
    }

    private static double rnd(double min, double max) {
        return min + (Math.random() * (max - min));
    }
}
