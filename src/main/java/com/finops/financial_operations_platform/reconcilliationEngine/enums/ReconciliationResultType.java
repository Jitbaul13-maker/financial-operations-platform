package com.finops.financial_operations_platform.reconcilliationEngine.enums;

public enum ReconciliationResultType {
    MATCHED,
    MISSING_INTERNAL,
    MISSING_PROVIDER,
    AMOUNT_MISMATCH,
    STATUS_MISMATCH,
    CURRENCY_MISMATCH,
    DUPLICATE,
    AMBIGUOUS_MATCH
}
