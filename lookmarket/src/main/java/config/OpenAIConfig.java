package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {
    @Value("${openai.api-base:https://api.openai.com/v1}") String apiBase;
    @Value("${openai.api-key}") String apiKey;
    
    @Bean
    WebClient openAIClient() {
        return WebClient.builder()
          .baseUrl(apiBase)
          .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .build();
    }

}
