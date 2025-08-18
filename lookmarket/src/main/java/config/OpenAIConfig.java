package config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
public class OpenAIConfig {

    @Bean(name = "openAIClient")
    @Primary
    WebClient openAIClient(
        @Value("${openai.api-base:https://api.openai.com/v1}") String apiBase,
        @Value("${OPENAI_API_KEY:${openai.api-key:}}")String apiKey,
        @Value("${openai.http.connect-timeout-ms:5000}") int connectTimeoutMs,
        @Value("${openai.http.response-timeout-ms:30000}") int responseTimeoutMs,
        @Value("${openai.proxy.enabled:false}") boolean proxyEnabled,
        @Value("${openai.proxy.host:}") String proxyHost,
        @Value("${openai.proxy.port:0}") int proxyPort
    ) {
        HttpClient http = HttpClient.create()
            // ✅ DNS 커스텀 제거: hosts 파일/OS 설정 그대로 사용
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
            .responseTimeout(Duration.ofMillis(responseTimeoutMs));

        if (proxyEnabled && proxyHost != null && !proxyHost.isBlank() && proxyPort > 0) {
            http = http.proxy(p -> p.type(ProxyProvider.Proxy.HTTP)
                                    .host(proxyHost)
                                    .port(proxyPort));
        }

        return WebClient.builder()
            .baseUrl(apiBase) // 끝에 /v1 유지
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(http))
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build())
            .build();
    }
}
