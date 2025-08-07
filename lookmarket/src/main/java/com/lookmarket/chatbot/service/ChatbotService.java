package com.lookmarket.chatbot.service;

import com.lookmarket.chatbot.utils.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("chatbotService")
public class ChatbotService {
    private final OpenAIClient openAIClient;

    public ChatbotService(@Value("${openai.api.key}") String apiKey) {
        this.openAIClient = new OpenAIClient(apiKey);
    }

    public String getChatbotResponse(String userMessage) throws Exception {
        // OpenAI API 호출
        String responseJson = openAIClient.sendChatMessage(userMessage);
        // TODO: JSON 파싱해서 필요한 데이터만 추출할 수도 있음
        return responseJson;
    }
}