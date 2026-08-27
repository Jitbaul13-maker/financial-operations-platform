package com.finops.financial_operations_platform.reconciliationEngine.enums;

import lombok.Getter;

@Getter
public enum ReconciliationSeverity {
    NONE(0),
    CRITICAL(4),
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    private final int priority;

    ReconciliationSeverity(int priority) {
        this.priority = priority;
    }
}
