package com.ai.integration.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {

    private final String status;
    private final String message;
    private final T data;
    private static final String SUCCESS_MESSAGE = "Data Fetched Successfully";

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(SUCCESS_MESSAGE)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .build();
    }
}
