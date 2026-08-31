package com.finops.financial_operations_platform.user.controller;

import com.finops.financial_operations_platform.user.Dto.LoginRequest;
import com.finops.financial_operations_platform.user.Dto.LoginResponse;
import com.finops.financial_operations_platform.user.Dto.RegisterRequest;
import com.finops.financial_operations_platform.user.Dto.RegisterResponse;
import com.finops.financial_operations_platform.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "APIs for user registration and authentication"
)
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            description = "Registers a new user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "User registration failed due to an internal runtime error"
            )
    })
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

        RegisterResponse response = service.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns authentication credentials."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed"
            )
    })
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest request) {

        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }
}
