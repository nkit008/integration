package com.ai.integration.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ContentUtility {

    public static List<String> parseStringToList(String value) {
        if (!StringUtils.hasText(value)) {
            return Collections.emptyList();
        }

        // Remove [ ] brackets
        String cleaned = value.trim()
                .replaceAll("^\\[", "")   // remove leading [
                .replaceAll("\\]$", "")   // remove trailing ]
                .trim();

        if (cleaned.isEmpty()) {
            return Collections.emptyList();
        }

        // Split by comma, clean each value
        return Arrays.stream(cleaned.split(","))
                .map(s -> s.trim()
                        .replace("\"", "")   // remove double quotes
                        .replace("'", ""))   // remove single quotes
                .filter(StringUtils::hasText)
                .toList();
    }
}
