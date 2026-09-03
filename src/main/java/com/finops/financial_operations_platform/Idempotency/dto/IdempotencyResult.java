package com.finops.financial_operations_platform.Idempotency.dto;

import com.finops.financial_operations_platform.Idempotency.enums.IdempotencyDecision;

public record IdempotencyResult(
        IdempotencyRecord record,
        IdempotencyDecision claimResult
) {}
