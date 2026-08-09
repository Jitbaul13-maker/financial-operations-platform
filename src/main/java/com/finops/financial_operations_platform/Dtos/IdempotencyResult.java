package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.businesslogics.IdempotencyRecord;
import com.finops.financial_operations_platform.enums.IdempotencyDecision;

public record IdempotencyResult(
        IdempotencyRecord record,
        IdempotencyDecision claimResult
) {}
