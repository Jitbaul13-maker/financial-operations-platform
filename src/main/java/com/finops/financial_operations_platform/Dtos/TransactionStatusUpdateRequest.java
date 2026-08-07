package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public record TransactionStatusUpdateRequest (
    @NotNull
    TransactionStatus status
){}
