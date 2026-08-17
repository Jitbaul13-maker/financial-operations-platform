package com.finops.financial_operations_platform.validations;

import com.finops.financial_operations_platform.Dtos.ProviderStatementRow;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProviderStatementValidator {

    private final Validator validator;

    public ProviderStatementValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(ProviderStatementRow statementRow) {
        Set<ConstraintViolation<ProviderStatementRow>> violations = validator.validate(statementRow);

        if (!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }
}
