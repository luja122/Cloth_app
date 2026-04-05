package com.spring.CloathingStore.dtos;

public record LoginResponse(
        String refreshToken,
        String accessToken,
        String typ,
        long expiredAt,
        Userdto user
) {
}
