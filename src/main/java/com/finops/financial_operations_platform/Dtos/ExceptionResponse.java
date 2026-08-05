package com.finops.financial_operations_platform.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {
    private String msg;
    private Integer status;
    private String error;
    private LocalDateTime time;
}
