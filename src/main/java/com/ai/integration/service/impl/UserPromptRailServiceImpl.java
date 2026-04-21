package com.ai.integration.service.impl;

import com.ai.integration.service.PerplexityProxyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.ai.integration.dto.UserPromptRailResponseDto;
import com.ai.integration.service.OpenAIProxyService;
import com.ai.integration.service.UserPromptRailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPromptRailServiceImpl implements UserPromptRailService {

    private static final String SYSTEM_PROMPT = """
                Role: You are an expert Content Strategist and Metadata Specialist who create rails for a OTT platform. 
                Your task is to analyze my raw content ideas and "decipher" them into structured filters for a content catalog. 
            
                Task: I will provide a description or a prompt idea . 
                You need to provide a valid json, If the input is abstract, thematic, or event-based (e.g., Holi, Diwali, festivals), you MUST infer genre, language, and contentType using the given values. Return null ONLY if there is absolutely no reasonable inference.
            
                Output Schema (JSON): 
                railTitle: (String) A broad, reusable category title (2-5 words) that could fit hundreds of similar titles. It must be a high-level bucket (e.g., "Gritty Crime Dramas" rather than "Movies about Batman"). MANDATORY. 
                genres: (Array or null) Choose ONLY from: [Drama, Thriller, Romance, Action, Comedy, Crime, Horror, Kids, Documentary, Sports, Biography, Spiritual]. 
                languages: (Array or null) Choose ONLY from: [Hindi, English, Kannada, Tamil, Telugu, Malayalam, Marathi, Bengali, Punjabi, Gujarati, Odia, Bhojpuri, Assamese, Haryanvi]. 
                yearOfReleaseFrom: (Integer or null). 
                - Represents the LOWER bound of release year.
                - Inclusive when used with BETWEEN or GREATER_THAN or EQUAL.
                yearOfReleaseTo: (Integer or null).
                - Represents the UPPER bound of release year.
                - Inclusive when used with BETWEEN or LESS_THAN.
                yearOfReleaseOperator: (String or null) Must follow STRICT rules:
                - "BETWEEN" → when both from and to exist (inclusive range)
                - ">" → strictly greater than (>)
                - ">=" → greater than or equal (>=) or from like form 2016
                - "<" → strictly less than (<)
                - "<=" → less than or equal (<=)  or upto like Holi Special upto 2016 or till 2016
                - "=" → exact year match like Holi Special 2016
               contentType: (String or Array) Must follow:
               - Return "Movies" if the intent clearly refers to films.
               - Return "TvShows" if the intent clearly refers to series.
               - Return ["Movies", "TvShows"] if both are equally relevant or unspecified (default case for broad/event-based prompts like festivals).
               - Return null ONLY if it cannot be inferred at all. 
               - For abstract, event-based, or festival prompts (e.g., Holi, Diwali), assume both Movies and TvShows unless explicitly restricted.
                pace: (Array or null) Comma-separated descriptive keywords (1-3 words each). Think in terms of : How fast or slow the story unfolds, How scenes transitions, how tension builds and releases. Key constraint : Purely structural / rhythmic No emotion, no ideas, no dialogue traits. 
                mood: (Array or null) Comma-separated descriptive keywords (1-3 words each). Describe emotional atmosphere and tone only. Focus on: How the story feels. Must NOT imply speed, narrative mechanics, themes, or dialogue quality. 
                theme: (Array or null) Comma-separated keywords (1-3 words each). Describe core narrative ideas or moral concepts. Focus on: Central conflicts, Philosophical or conceptual ideas. Must NOT describe emotions, pacing, or dialogue craft. Strict Rules: If a field cannot be determined, return null. 
            
                RailTitle Logic: Do not list 300 titles. Instead, generate the one or two best broad rail title that would allow this specific content to be grouped with hundreds of other similar titles in a 50,000-title library. Output MUST be valid JSON. No conversational filler. 
            """;

    private final OpenAIProxyService openAIProxyService;
    private final PerplexityProxyService perplexityProxyService;
    private final ObjectMapper objectMapper;

    @Override
    public UserPromptRailResponseDto executeUserPrompt(String userPrompt) {
        if (!StringUtils.hasText(userPrompt)) {
            throw new IllegalArgumentException("userPrompt must not be blank");
        }
        String raw = openAIProxyService.executeChatCompletion(userPrompt.trim(), SYSTEM_PROMPT);
        String json = stripMarkdownCodeFence(raw);
        log.debug("response from LLM model is :: {}", json);
        try {
            return objectMapper.readValue(json, UserPromptRailResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse OpenAI JSON for user prompt. Raw response: {}", raw, e);
            throw new IllegalStateException("Failed to parse model response as rail metadata JSON", e);
        }
    }

    @Override
    public UserPromptRailResponseDto executeUserPromptPerplexity(String userPrompt) {
        if (!StringUtils.hasText(userPrompt)) {
            throw new IllegalArgumentException("userPrompt must not be blank");
        }
        String raw = perplexityProxyService.chat(userPrompt, SYSTEM_PROMPT);
        String json = stripMarkdownCodeFence(raw);
        log.debug("response from LLM model is :: {}", json);
        try {
            return objectMapper.readValue(json, UserPromptRailResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse OpenAI JSON for user prompt. Raw response: {}", raw, e);
            throw new IllegalStateException("Failed to parse model response as rail metadata JSON", e);
        }
    }


    private String stripMarkdownCodeFence(String response) {
        if (response == null) {
            return "";
        }
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }
}
