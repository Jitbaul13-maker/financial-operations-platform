package com.finops.financial_operations_platform.Exceptions;

public class BusinessRuleNotFoundException extends RuntimeException{
    public BusinessRuleNotFoundException(String message) {
        super(message);
    }
}
