package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountLimitRule implements TransactionRule {

    BigDecimal maximumAmount = BigDecimal.valueOf(99999);

    @Override
    public RuleResult evaluate(TransactionContext context) {

        BigDecimal amount = context.amount();
        String ruleCode = "AMOUNT_RULE";

        if (amount.compareTo(maximumAmount) > 0) {
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
