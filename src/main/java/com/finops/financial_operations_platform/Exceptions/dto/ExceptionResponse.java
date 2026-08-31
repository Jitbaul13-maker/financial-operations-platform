package com.finops.financial_operations_platform.Exceptions.dto;

import java.time.OffsetDateTime;

public record ExceptionResponse (
    String msg,
    Integer status,
    String error,
    OffsetDateTime time
){}
