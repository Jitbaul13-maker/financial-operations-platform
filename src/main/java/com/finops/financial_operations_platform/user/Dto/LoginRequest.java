package com.finops.financial_operations_platform.user.Dto;

public record LoginRequest(
        String username,
        String password
) {}