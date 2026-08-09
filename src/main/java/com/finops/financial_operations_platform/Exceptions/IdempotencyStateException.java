package com.finops.financial_operations_platform.Exceptions;

public class IdempotencyStateException extends RuntimeException{
    public IdempotencyStateException(String message) {
        super(message);
    }
}
