package com.finops.financial_operations_platform.rules.implementations;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleConfigurationService;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProviderCurrencyRule implements TransactionRule {

    private final RuleConfigurationService configurationService;

    public ProviderCurrencyRule(RuleConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

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

        Map<String, List<String>> map = configurationService.getRequiredMap(ruleCode, "supportedCurrencies");

        List<String> currencies = map.get(provider.name());

        if (currencies != null && currencies.contains(currency)) {
            return result;
        }

        return new RuleResult(
                ruleCode,
                RuleDecision.REJECT,
                "Provider does not support transaction with requested currency!"
        );
    }
}
