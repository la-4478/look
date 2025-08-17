package com.lookmarket.Vector;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbeddingService {
	private WebClient openAIClient; // 네가 만든 WebClient 빈

    public float[] embed(String text) {
        Map<String, Object> req = Map.of(
            "model", "text-embedding-3-small", // 가성비 모델 추천
            "input", text
        );
        Map res = openAIClient.post()
            .uri("/embeddings")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        List<Double> v = (List<Double>) ((Map)((List)res.get("data")).get(0)).get("embedding");
        float[] out = new float[v.size()];
        for (int i = 0; i < v.size(); i++) out[i] = v.get(i).floatValue();
        return out;
    }
}
