package com.finops.financial_operations_platform.Exceptions;

public class TransactionRuleRejectedException extends RuntimeException{
    public TransactionRuleRejectedException(String message) {
        super(message);
    }
}
