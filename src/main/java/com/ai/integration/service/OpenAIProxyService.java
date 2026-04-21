package com.ai.integration.service;


public interface OpenAIProxyService {
    /**
     * Executes a chat completion using the same OpenAI client configuration as other rail flows.
     *
     * @param userPrompt   user message
     * @param systemPrompt system message
     * @return raw assistant message content (not parsed)
     */
    String executeChatCompletion(String userPrompt, String systemPrompt);
}