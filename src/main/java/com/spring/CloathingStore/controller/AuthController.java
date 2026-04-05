package com.spring.CloathingStore.controller;

import com.spring.CloathingStore.dtos.LoginRequest;
import com.spring.CloathingStore.dtos.LoginResponse;
import com.spring.CloathingStore.dtos.Userdto;
import com.spring.CloathingStore.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/Register")
    public ResponseEntity<?> register(@RequestBody Userdto data){
      String response = authService.register(data);
      return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody LoginRequest data){
        LoginResponse loginResponse = authService.login(response,data);
        return ResponseEntity.ok(loginResponse);
    }
}
