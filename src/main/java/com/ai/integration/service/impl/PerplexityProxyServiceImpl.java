package com.ai.integration.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import com.ai.integration.config.PerplexityPropertiesConfig;
import com.ai.integration.dto.llm.PerplexityChatRequest;
import com.ai.integration.dto.llm.PerplexityChatResponse;
import com.ai.integration.exception.LlmServiceException;
import com.ai.integration.service.PerplexityProxyService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerplexityProxyServiceImpl implements PerplexityProxyService {

    private final RestClient restClient;
    private final PerplexityPropertiesConfig config;

    @Override
    public String chat(String userPrompt, String systemPrompt) {
        PerplexityChatRequest request = PerplexityChatRequest.builder()
                .model(config.getApi().getModel())
                .messages(List.of(
                        new PerplexityChatRequest.Message("system", systemPrompt),
                        new PerplexityChatRequest.Message("user", userPrompt)
                ))
                .temperature(config.getApi().getDefaultTemperature())
                .maxTokens(config.getApi().getDefaultMaxTokens())
                .build();

        try {
            PerplexityChatResponse response = restClient.post()
                    .uri("") // baseUrl already set in PerplexityRestClientConfig
                    .body(request)
                    .retrieve()
                    .body(PerplexityChatResponse.class);

            if (response == null || response.firstChoiceContent() == null) {
                log.error("Perplexity returned empty or null response for model={}", config.getApi().getModel());
                throw new LlmServiceException("Perplexity", "Perplexity returned an empty response", null);
            }

            log.debug("Perplexity raw content: {}", response.firstChoiceContent());
            return response.firstChoiceContent();

        } catch (LlmServiceException e) {
            throw e;
        } catch (RestClientException e) {
            log.error("Perplexity API call failed — model={}, error={}", config.getApi().getModel(), e.getMessage(), e);
            throw new LlmServiceException("Perplexity", "Perplexity request failed: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Perplexity unexpected error — model={}, error={}", config.getApi().getModel(), e.getMessage(), e);
            throw new LlmServiceException("Perplexity", "Unexpected Perplexity error: " + e.getMessage(), e);
        }
    }
}
