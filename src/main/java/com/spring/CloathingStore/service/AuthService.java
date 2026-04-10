package com.spring.CloathingStore.service;

import com.spring.CloathingStore.dtos.LoginRequest;
import com.spring.CloathingStore.dtos.LoginResponse;
import com.spring.CloathingStore.dtos.Userdto;
import com.spring.CloathingStore.model.RefreshTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthService {
    public String register(Userdto data);

    LoginResponse login(HttpServletResponse response, LoginRequest data);

    Optional<?> readFromToken(HttpServletRequest request, RefreshTokenRequest body);

    Optional<?> readFromAccessToken(HttpServletRequest request);
}
