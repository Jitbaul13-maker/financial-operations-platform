package com.finops.financial_operations_platform.Idempotency.dto;

import com.finops.financial_operations_platform.Idempotency.enums.IdempotencyStatus;

public record IdempotencyRecord (
    IdempotencyStatus status,
    String requestFingerprint,
    String transactionId
){}
