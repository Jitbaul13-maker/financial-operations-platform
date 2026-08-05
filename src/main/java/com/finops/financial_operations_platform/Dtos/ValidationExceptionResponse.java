package com.finops.financial_operations_platform.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ValidationExceptionResponse {
    private String msg;
    private Integer status;
    private String error;
    private LocalDateTime time;
    private Map<String, String> errors;
}
