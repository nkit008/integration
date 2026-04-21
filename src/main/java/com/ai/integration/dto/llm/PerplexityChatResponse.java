package com.ai.integration.dto.llm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Minimal mapping of the Perplexity chat-completions response envelope.
 * Only the fields needed to extract the assistant's message content are mapped.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerplexityChatResponse {

    private List<Choice> choices;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Message message;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String role;
        private String content;
    }

    /**
     * Convenience method — returns the assistant's reply text from the first choice.
     */
    public String firstChoiceContent() {
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        Choice first = choices.get(0);
        if (first == null || first.getMessage() == null) {
            return null;
        }
        return first.getMessage().getContent();
    }
}
