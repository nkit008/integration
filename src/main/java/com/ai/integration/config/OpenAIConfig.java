package com.ai.integration.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configures a OpenAiService.
 */
@Configuration
public class OpenAIConfig {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.timeout:60}")  // default 60 seconds
    private long timeout;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey, Duration.ofSeconds(timeout));
    }
} 