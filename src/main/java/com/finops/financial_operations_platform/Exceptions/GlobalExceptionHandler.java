package com.finops.financial_operations_platform.Exceptions;

import com.finops.financial_operations_platform.Dtos.ExceptionResponse;
import com.finops.financial_operations_platform.Dtos.ValidationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> transactionNotFoundException(TransactionNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        "TRANSACTION_NOT_FOUND",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidTransactionStateTransitionException.class)
    public ResponseEntity<ExceptionResponse> InvalidTransactionStateTransitionException
            (InvalidTransactionStateTransitionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value(),
                        "Invalid Transaction Request",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ValidationExceptionResponse(
                        "Request validation failed",
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_FAILED",
                        LocalDateTime.now(),
                        errors
                )
        );
    }
}
