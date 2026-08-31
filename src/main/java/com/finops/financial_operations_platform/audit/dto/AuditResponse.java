package com.finops.financial_operations_platform.audit.dto;

import com.finops.financial_operations_platform.enums.TransactionStatus;

import java.time.OffsetDateTime;

public record AuditResponse(
        String transactionId,
        TransactionStatus oldStatus,
        TransactionStatus newStatus,
        String actor,
        String reason,
        OffsetDateTime createdAt
) {}