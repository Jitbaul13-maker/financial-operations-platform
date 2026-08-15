package com.finops.financial_operations_platform.Exceptions;

public class InactiveBusinessRuleException extends RuntimeException{
    public InactiveBusinessRuleException(String message) {
        super(message);
    }
}
