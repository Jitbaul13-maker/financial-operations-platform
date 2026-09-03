package com.finops.financial_operations_platform.Idempotency.enums;

public enum IdempotencyDecision {
    ACQUIRED,
    IN_PROGRESS,
    COMPLETED,
    CONFLICT
}
