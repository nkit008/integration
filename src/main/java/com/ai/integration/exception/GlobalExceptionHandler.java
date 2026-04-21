package com.ai.integration.exception;

import com.ai.integration.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElse(null);

        String message;
        if (error != null) {
            message = formatFieldError(error);
        } else {
            message = "Validation failed";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(message));
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + " " + error.getDefaultMessage();
    }

    @ExceptionHandler(LlmServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleLlmServiceException(LlmServiceException e) {
        log.error("[{}] LLM service error: {}", e.getProvider(), e.getMessage(), e);
        String message = "[" + e.getProvider() + "] " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ApiResponse.error(message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception e) {
        return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage()));
    }
}
