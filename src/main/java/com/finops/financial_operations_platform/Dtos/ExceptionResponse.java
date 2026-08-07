package com.finops.financial_operations_platform.Dtos;

import java.time.OffsetDateTime;

public record ExceptionResponse (
    String msg,
    Integer status,
    String error,
    OffsetDateTime time
){}
