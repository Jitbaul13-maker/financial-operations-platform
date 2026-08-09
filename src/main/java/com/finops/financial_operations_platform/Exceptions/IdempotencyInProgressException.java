package com.finops.financial_operations_platform.Exceptions;

public class IdempotencyInProgressException extends RuntimeException{
    public IdempotencyInProgressException(String message) {
        super(message);
    }
}
