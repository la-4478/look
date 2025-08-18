package com.lookmarket.Vector;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiAnswerService {
    private  EmbeddingService embeddingService;
    private  VectorIndex vectorIndex;
    private  WebClient openAIClient;

    private static final String SYSTEM_PROMPT = """
    너는 우리 회사 홈페이지 전용 도우미야.
    - 반드시 제공된 컨텍스트(홈페이지에서 추출/등록한 내용)만 근거로 답한다.
    - 컨텍스트에 없거나 홈페이지와 무관한 질문이면 "저는 홈페이지 관련 질문만 도와드려요..."라고 정중히 안내하고, 추측하지 않는다.
    - 답변은 한국어로 간결하게, 링크/경로(있다면)도 함께 제시.
    """;

    public Answer answer(String userQuestion){
        float[] qv = embeddingService.embed(userQuestion);
        var hits = vectorIndex.topK(qv, 5);

        double best = hits.isEmpty()? 0.0 : hits.get(0).score();
        if (best < 0.25) { // 임계값: 데이터 없음/범위 밖
            return new Answer("제가 도와드리는 범위는 홈페이지 관련 안내예요. 예: 회원가입, 로그인 문제, 주문/배송 안내, 게시판 이용 등. 질문을 조금 더 구체화해 주세요!", List.of());
        }

        String context = hits.stream()
            .map(h -> "### " + h.title() + "\n" + clip(h.text(), 1200))
            .reduce((a,b)->a+"\n\n---\n\n"+b).orElse("");

        Map<String, Object> req = Map.of(
          "model", "gpt-4o-mini",   // 가성비 모델 예시(네가 쓰는 걸로 바꿔도 됨)
          "messages", List.of(
            Map.of("role","system","content", SYSTEM_PROMPT),
            Map.of("role","user","content", "질문:\n" + userQuestion + "\n\n컨텍스트(홈페이지):\n" + context)
          ),
          "temperature", 0.2
        );

        Map res = openAIClient.post().uri("/chat/completions").bodyValue(req)
            .retrieve().bodyToMono(Map.class).block();

        String content = (String) ((Map)((Map)((List)res.get("choices")).get(0)).get("message")).get("content");

        // 출처(타이틀만 간단히)
        List<String> sources = hits.stream().map(VectorIndex.SearchHit::title).toList();
        return new Answer(content, sources);
    }

    private static String clip(String s, int max){ return s.length()<=max? s : s.substring(0, max) + "..."; }

    public record Answer(String content, List<String> sources) {}
}
