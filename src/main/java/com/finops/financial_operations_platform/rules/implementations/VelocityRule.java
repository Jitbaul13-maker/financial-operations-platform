package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.*;
import org.springframework.stereotype.Component;

@Component
public class VelocityRule implements TransactionRule {

    private final VelocityCounterService counterService;
    private final RuleConfigurationService configurationService;

    public VelocityRule(VelocityCounterService counterService, RuleConfigurationService configurationService) {
        this.counterService = counterService;
        this.configurationService = configurationService;
    }

    String ruleCode = "VELOCITY_RULE";

    @Override
    public RuleResult evaluate(TransactionContext context) {

        Long windowMinutes = configurationService.getRequiredLong(ruleCode, "windowMinutes");
        long count = counterService.incrementCounter(context.customerId(), windowMinutes);
        long maxCount = configurationService.getRequiredLong(ruleCode, "maxTransactions");

        if (count > maxCount) {
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
