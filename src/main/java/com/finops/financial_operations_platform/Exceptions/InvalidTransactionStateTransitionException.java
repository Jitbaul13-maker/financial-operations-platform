package com.finops.financial_operations_platform.Exceptions;

public class InvalidTransactionStateTransitionException extends RuntimeException{
    public InvalidTransactionStateTransitionException(String message) {
        super(message);
    }
}
