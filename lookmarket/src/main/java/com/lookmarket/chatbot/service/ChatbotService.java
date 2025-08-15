package com.lookmarket.chatbot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("chatbotService")
public class ChatbotService {
    @Autowired
    private WebClient openAIClient; // OpenAIConfig에서 만든 WebClient 빈

    public String askChatGPT(String userMessage) {
        // OpenAI Chat API 요청 JSON 생성
        Map<String, Object> requestBody = Map.of(
            "model", "gpt-4o-mini",
            "messages", List.of(
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", userMessage)
            ),
            "max_tokens", 500
        );

        // API 호출
        String responseJson = openAIClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 호출 (필요하면 async로 변경 가능)

        return responseJson; // JSON 문자열 그대로 반환 (파싱 가능)
    }
}