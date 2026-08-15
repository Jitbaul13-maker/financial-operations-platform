package com.finops.financial_operations_platform.rules;

import com.finops.financial_operations_platform.enums.Provider;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionContext (
        String transactionId,
        String providerTransactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        Provider provider,
        Instant timestamp
){}
