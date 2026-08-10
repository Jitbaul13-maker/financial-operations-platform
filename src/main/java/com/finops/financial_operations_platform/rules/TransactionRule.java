package com.finops.financial_operations_platform.rules;

public interface TransactionRule {
    RuleResult evaluate(TransactionContext context);
}
