package com.finops.financial_operations_platform.businesslogics;

import com.finops.financial_operations_platform.enums.IdempotencyStatus;

public record IdempotencyRecord (
    IdempotencyStatus status,
    String requestFingerprint,
    String transactionId
){}
