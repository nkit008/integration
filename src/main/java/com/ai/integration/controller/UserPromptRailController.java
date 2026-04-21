package com.ai.integration.controller;

import com.ai.integration.dto.UserPromptRailResponseDto;
import com.ai.integration.dto.response.ApiResponse;
import com.ai.integration.service.UserPromptRailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user-prompt")
@Validated
@RequiredArgsConstructor
public class UserPromptRailController {

    private final UserPromptRailService userPromptRailService;

    /**
     * Executes the user prompt against ChatGPT and returns structured rail metadata.
     * Example: GET .../rail-metadata?userPrompt=...
     */
    @PostMapping("/rail-metadata/chatgpt")
    public ResponseEntity<ApiResponse<UserPromptRailResponseDto>> getRailMetadataFromUserPrompt(@RequestBody String userPrompt) {
        log.info("user-prompt rail-metadata request, prompt:: {}", userPrompt);
        UserPromptRailResponseDto dto = userPromptRailService.executeUserPrompt(userPrompt);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping("/rail-metadata/perplexity")
    public ResponseEntity<ApiResponse<UserPromptRailResponseDto>> getRailMetadataFromUserPrompt1(@RequestBody String userPrompt) {
        log.info("user-prompt rail-metadata request, prompt:: {}", userPrompt);
        UserPromptRailResponseDto dto = userPromptRailService.executeUserPrompt(userPrompt);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
