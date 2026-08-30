package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.enums.Provider;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateTransactionRequest (

    @NotNull
    Provider provider,

    @NotNull
    String providerTransactionId,

    @NotNull
    @Positive
    BigDecimal amount,

    @NotBlank
    @Size(min = 3, max = 3)
    String currency
){}
