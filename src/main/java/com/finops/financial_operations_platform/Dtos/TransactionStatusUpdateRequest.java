package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TransactionStatusUpdateRequest {
    @NotNull
    TransactionStatus status;
}
