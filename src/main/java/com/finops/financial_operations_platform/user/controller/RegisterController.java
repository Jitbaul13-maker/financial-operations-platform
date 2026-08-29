package com.finops.financial_operations_platform.user.controller;

import com.finops.financial_operations_platform.user.Dto.RegisterRequest;
import com.finops.financial_operations_platform.user.Dto.RegisterResponse;
import com.finops.financial_operations_platform.user.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService service;

    @PostMapping
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {

        RegisterResponse response = service.register(request);

        return ResponseEntity.ok(response);
    }
}
