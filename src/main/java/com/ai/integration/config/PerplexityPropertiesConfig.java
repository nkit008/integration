package com.ai.integration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "perplexity")
@Data
public class PerplexityPropertiesConfig {

    private Api api;

    @Data
    public static class Api {
        private String url;
        private String key;
        private String model;
        private int timeoutSeconds;
        private double defaultTemperature;
        private int defaultMaxTokens;
    }
}
