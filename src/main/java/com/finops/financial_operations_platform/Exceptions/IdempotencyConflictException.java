package com.finops.financial_operations_platform.Exceptions;

public class IdempotencyConflictException extends RuntimeException{
    public IdempotencyConflictException(String message) {
        super(message);
    }
}
