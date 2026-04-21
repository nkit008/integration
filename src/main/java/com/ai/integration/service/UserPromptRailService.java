package com.ai.integration.service;

import com.ai.integration.dto.UserPromptRailResponseDto;

public interface UserPromptRailService {

    /**
     * Sends the user prompt to ChatGPT (via existing OpenAI wiring) and maps the reply to
     * {@link UserPromptRailResponseDto}.
     *
     * @param userPrompt free-text prompt from the FE
     * @return parsed structured metadata
     */
    UserPromptRailResponseDto executeUserPrompt(String userPrompt);
    UserPromptRailResponseDto executeUserPromptPerplexity(String userPrompt);
}
