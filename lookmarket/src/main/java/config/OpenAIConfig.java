package config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class OpenAIConfig {
    @Value("${openai.api-base:https://api.openai.com/v1}") String apiBase;
    @Value("${openai.api-key}") String apiKey;

    // application.properties 에 이미 있음:
    // openai.http.connect-timeout-ms=5000
    // openai.http.read-timeout-ms=30000
    @Value("${openai.http.connect-timeout-ms:5000}") int connectTimeoutMs;
    @Value("${openai.http.read-timeout-ms:30000}") int readTimeoutMs;

    @Bean
    WebClient openAIClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(readTimeoutMs));

        return WebClient.builder()
                .baseUrl(apiBase)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}