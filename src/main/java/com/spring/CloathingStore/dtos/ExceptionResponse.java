package com.spring.CloathingStore.dtos;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(
        String message,
        HttpStatus status,
        int statuscode
) {
}
