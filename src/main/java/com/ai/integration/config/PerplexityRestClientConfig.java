package com.ai.integration.config;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class PerplexityRestClientConfig {

    @Bean
    public RestClient restClient(PerplexityPropertiesConfig config) {

        ClientHttpRequestFactorySettings settings =
                ClientHttpRequestFactorySettings.DEFAULTS
                        .withConnectTimeout(Duration.ofSeconds(config.getApi().getTimeoutSeconds()))
                        .withReadTimeout(Duration.ofSeconds(config.getApi().getTimeoutSeconds()));

        return RestClient.builder()
                .baseUrl(config.getApi().getUrl())
                .defaultHeader("Authorization", "Bearer " + config.getApi().getKey())
                .defaultHeader("Content-Type", "application/json")
                .requestFactory(ClientHttpRequestFactories.get(settings))
                .build();
    }
}
