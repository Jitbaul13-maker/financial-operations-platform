package com.finops.financial_operations_platform.user.controller;

import com.finops.financial_operations_platform.user.Dto.LoginRequest;
import com.finops.financial_operations_platform.user.Dto.LoginResponse;
import com.finops.financial_operations_platform.user.Dto.RegisterRequest;
import com.finops.financial_operations_platform.user.Dto.RegisterResponse;
import com.finops.financial_operations_platform.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {

        RegisterResponse response = service.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> register(LoginRequest request) {

        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }
}
