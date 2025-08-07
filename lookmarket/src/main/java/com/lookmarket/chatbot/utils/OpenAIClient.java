package com.lookmarket.chatbot.utils;

import okhttp3.*;
import java.io.IOException;

public class OpenAIClient {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public OpenAIClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String sendChatMessage(String userMessage) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String json = "{"
            + "\"model\": \"gpt-3.5-turbo\","
            + "\"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}]"
            + "}";

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
            .url(OPENAI_API_URL)
            .header("Authorization", "Bearer " + apiKey)
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}