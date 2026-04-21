package com.ai.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Structured rail metadata returned to the FE after interpreting the user's prompt via ChatGPT.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPromptRailResponseDto {

    private String railTitle;
    private String railDescription;
    private List<String> contentType;
    private List<String> genres;
    private List<String> languages;
    private String yearOfReleaseFrom;
    private String yearOfReleaseTo;
    private String yearOfReleaseOperator;
    private String useCaseShortName;
    private List<String> pace;
    private List<String> mood;
    private List<String> theme;
}
