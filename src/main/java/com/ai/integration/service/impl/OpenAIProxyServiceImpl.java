package com.ai.integration.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ai.integration.exception.LlmServiceException;
import com.ai.integration.service.OpenAIProxyService;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIProxyServiceImpl implements OpenAIProxyService {

    private final ObjectMapper objectMapper;
    private final OpenAiService openAiService;

    @Value("${openai.model}")
    private String openAiModel;

    @Override
    public String executeChatCompletion(String userPrompt, String systemPrompt) {
        return executeWithRetry(() -> getChatCompletion(userPrompt, systemPrompt));
    }

    private String getChatCompletion(String userPrompt, String systemPrompt) {
        try {
            ChatMessage systemMessage = new ChatMessage("system", systemPrompt);
            ChatMessage userMessage = new ChatMessage("user", userPrompt);

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(openAiModel)
                    .messages(Arrays.asList(systemMessage, userMessage))
                    .build();

            return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("OpenAI API call failed — model={}, error={}", openAiModel, e.getMessage(), e);
            throw new LlmServiceException("OpenAI", "OpenAI request failed: " + e.getMessage(), e);
        }
    }

    private <T> T executeWithRetry(RetryableOperation<T> operation) {
        try {
            return operation.execute();
        } catch (LlmServiceException e) {
            throw e;
        } catch (JsonProcessingException e) {
            log.error("OpenAI JSON processing error: {}", e.getMessage(), e);
            throw new LlmServiceException("OpenAI", "Failed to process OpenAI response: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("OpenAI unexpected error: {}", e.getMessage(), e);
            throw new LlmServiceException("OpenAI", "Unexpected OpenAI error: " + e.getMessage(), e);
        }
    }


    @FunctionalInterface
    private interface RetryableOperation<T> {
        T execute() throws Exception;
    }
}