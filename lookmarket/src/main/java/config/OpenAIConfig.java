package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * ✨ OpenAIConfig
 * - application.properties 의 openai.api-base / openai.api-key 를 읽어 WebClient 빈 생성
 * - 기본 Authorization / Content-Type 헤더를 미리 셋업해 두어 서비스단에서 재사용
 */
@Configuration
public class OpenAIConfig {
    // 기본값은 공식 v1 엔드포인트. 사설 프록시/게이트웨이를 쓸 땐 값을 오버라이드
    @Value("${openai.api-base:https://api.openai.com/v1}") String apiBase;
    // 키는 대시(-) 키로 통일. properties에서도 동일 키를 사용해야 주입됨
    @Value("${openai.api-key}") String apiKey;

    @Bean
    WebClient openAIClient() {
        return WebClient.builder()
          .baseUrl(apiBase)
          // 모든 요청에 공통으로 들어갈 헤더
          .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .build();
    }
}
