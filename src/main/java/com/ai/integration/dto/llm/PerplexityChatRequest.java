package com.ai.integration.dto.llm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
public class PerplexityChatRequest {
    String model;
    List<Message> messages;
    double temperature;
    @JsonProperty("max_tokens")
    int maxTokens;

    @Getter
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
