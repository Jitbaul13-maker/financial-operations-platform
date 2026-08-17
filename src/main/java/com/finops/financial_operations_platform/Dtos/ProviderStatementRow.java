package com.finops.financial_operations_platform.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProviderStatementRow(
        @NotBlank
        String provider,
        @NotBlank
        String providerTransactionId,
        @NotNull
        @Positive
        BigDecimal amount,
        @NotBlank
        String currency,
        @NotBlank
        String status,
        @NotNull
        OffsetDateTime createdAt
) {}
