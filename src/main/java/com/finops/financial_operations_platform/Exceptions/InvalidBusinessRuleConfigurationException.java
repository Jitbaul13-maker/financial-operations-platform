package com.finops.financial_operations_platform.Exceptions;

public class InvalidBusinessRuleConfigurationException extends RuntimeException{
    public InvalidBusinessRuleConfigurationException(String message) {
        super(message);
    }
}
