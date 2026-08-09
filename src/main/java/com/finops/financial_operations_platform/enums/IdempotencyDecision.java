package com.finops.financial_operations_platform.enums;

public enum IdempotencyDecision {
    ACQUIRED,
    IN_PROGRESS,
    COMPLETED,
    CONFLICT
}
