package com.finops.financial_operations_platform.Dtos;

import java.time.OffsetDateTime;
import java.util.Map;

public record ValidationExceptionResponse (
    String msg,
    Integer status,
    String error,
    OffsetDateTime time,
    Map<String, String> errors
){}
