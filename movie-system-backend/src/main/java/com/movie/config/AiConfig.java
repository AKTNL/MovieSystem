package com.movie.config;

import ai.z.openapi.ZhipuAiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Value("${zhipu.api-key}")
    private String apiKey;

    @Bean
    public ZhipuAiClient zhipuAiClient() {
        return ZhipuAiClient.builder()
                .apiKey(apiKey)
                .build();
    }
}
