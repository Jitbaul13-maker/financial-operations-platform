package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.models.BusinessRule;
import com.finops.financial_operations_platform.rules.RuleConfigurationService;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountLimitRule implements TransactionRule {

    private final RuleConfigurationService ruleConfigurationService;

    public AmountLimitRule(RuleConfigurationService ruleConfigurationService) {
        this.ruleConfigurationService = ruleConfigurationService;
    }

    @Override
    public RuleResult evaluate(TransactionContext context) {

        BigDecimal amount = context.amount();
        String ruleCode = "AMOUNT_RULE";

        BigDecimal maxAmount = ruleConfigurationService
                .getRequiredDecimal(ruleCode, "maxAmount");

        if (amount.compareTo(maxAmount) > 0) {
            return new RuleResult(
                    ruleCode,
                    RuleDecision.REJECT,
                    "Transaction amount exceeds the permitted limit"
            );
        } else {
            return new RuleResult(
                    ruleCode,
                    RuleDecision.PASS,
                    "Transaction amount is within the permitted limit"
            );
        }
    }
}
