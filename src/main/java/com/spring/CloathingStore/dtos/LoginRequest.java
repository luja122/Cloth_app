package com.spring.CloathingStore.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
