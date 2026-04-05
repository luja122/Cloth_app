package com.spring.CloathingStore.service;

import com.spring.CloathingStore.dtos.LoginRequest;
import com.spring.CloathingStore.dtos.LoginResponse;
import com.spring.CloathingStore.dtos.Userdto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public String register(Userdto data);

    LoginResponse login(HttpServletResponse response, LoginRequest data);
}
