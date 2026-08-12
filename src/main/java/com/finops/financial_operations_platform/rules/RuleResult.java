package com.finops.financial_operations_platform.rules;

import com.finops.financial_operations_platform.enums.RuleDecision;

public record RuleResult(
        String ruleCode,
        RuleDecision decision,
        String reason
) {}
