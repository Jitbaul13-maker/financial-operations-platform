package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import com.finops.financial_operations_platform.rules.VelocityCounterService;
import org.springframework.stereotype.Component;

@Component
public class VelocityRule implements TransactionRule {

    private final VelocityCounterService counterService;

    public VelocityRule(VelocityCounterService counterService) {
        this.counterService = counterService;
    }

    String ruleCode = "VELOCITY_RULE";

    @Override
    public RuleResult evaluate(TransactionContext context) {
        long count = counterService.incrementCounter(context.customerId());

        if (count > 5) {
            return new RuleResult(
                    ruleCode,
                    RuleDecision.FLAG,
                    "Customer has exceeded the permitted transaction frequency within the configured time window."
            );
        }
        else return new RuleResult(
                ruleCode,
                RuleDecision.PASS,
                "Customer transaction frequency is within the permitted limit."
        );
    }
}
