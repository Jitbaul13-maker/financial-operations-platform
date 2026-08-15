package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.springframework.stereotype.Component;

@Component
public class ProviderCurrencyRule implements TransactionRule {

    @Override
    public RuleResult evaluate(TransactionContext context) {
        Provider provider = context.provider();
        String currency = context.currency();

        String ruleCode = "PROVIDER_RULE";
        RuleResult result = new RuleResult(
                ruleCode,
                RuleDecision.PASS,
                "Provider supports transaction with requested currency."
        );

        if (provider == Provider.RAZORPAY && currency.equals("INR")) return result;
        if (provider == Provider.WALLET && currency.equals("INR")) return result;
        if (provider == Provider.PAYPAL && currency.equals("USD")) return result;

        return new RuleResult(
                ruleCode,
                RuleDecision.REJECT,
                "Provider does not support transaction with requested currency!"
        );
    }
}
