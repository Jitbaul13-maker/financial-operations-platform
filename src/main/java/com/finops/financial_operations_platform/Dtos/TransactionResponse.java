package com.finops.financial_operations_platform.Dtos;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponse {

    private String customerId;
    private String transactionId;
    private Provider provider;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
