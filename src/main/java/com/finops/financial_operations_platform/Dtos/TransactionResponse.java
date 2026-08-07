package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionResponse (

    String customerId,
    String transactionId,
    Provider provider,
    BigDecimal amount,
    String currency,
    TransactionStatus status,

    OffsetDateTime createdAt,
    OffsetDateTime updatedAt

){}
