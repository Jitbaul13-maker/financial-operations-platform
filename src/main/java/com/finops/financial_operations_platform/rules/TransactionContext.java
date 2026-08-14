package com.finops.financial_operations_platform.rules;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionContext (
        String transactionId,
        String providerTransactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        String provider,
        Instant timestamp
){}
