package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.enums.Provider;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateTransactionRequest {

    @NotNull
    private Provider provider;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;

    @NotBlank
    private String customerId;

}
