package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class ProviderCurrencyRule implements TransactionRule {

    @Override
    public RuleResult evaluate(TransactionContext context) {
        String provider = context.provider();
        String currency = context.currency();

        String ruleCode = "PROVIDER_RULE";
        RuleResult result = new RuleResult(
                ruleCode,
                RuleDecision.PASS,
                "Provider supports transaction with requested currency."
        );

        if (provider.equals("RAZORPAY") && currency.equals("INR")) return result;
        if (provider.equals("WALLET") && currency.equals("INR")) return result;
        if (provider.equals("STRIPE") && currency.equals("USD")) return result;

        return new RuleResult(
                ruleCode,
                RuleDecision.REJECT,
                "Provider does not support transaction with requested currency!"
        );
    }
}
