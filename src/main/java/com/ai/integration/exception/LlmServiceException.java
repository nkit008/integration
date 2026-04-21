package com.ai.integration.exception;

/**
 * Thrown when an LLM provider (OpenAI / Perplexity) call fails.
 * Carries the provider name so the FE response is descriptive.
 */
public class LlmServiceException extends RuntimeException {

    private final String provider;

    public LlmServiceException(String provider, String message, Throwable cause) {
        super(message, cause);
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}
