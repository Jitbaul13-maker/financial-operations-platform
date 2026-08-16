package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleConfigurationService;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.implementations.ProviderCurrencyRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProviderCurrencyRuleTest {
    @Mock
    TransactionContext context;

    @Mock
    RuleConfigurationService configurationService;

    @InjectMocks
    ProviderCurrencyRule currencyRule;

    @Test
    void shouldPassWhenProviderSupportsCurrency() {

        when(context.currency()).thenReturn("INR");
        when(context.provider()).thenReturn(Provider.WALLET);
        when(configurationService.getRequiredMap("PROVIDER_RULE", "supportedCurrencies"))
                .thenReturn(Map.of("WALLET", List.of("INR")));

        RuleResult result = currencyRule.evaluate(context);

        assertEquals(RuleDecision.PASS, result.decision());
    }

    @Test
    void shouldRejectWhenProviderDoesNotSupportsCurrency() {

        when(context.currency()).thenReturn("USD");
        when(context.provider()).thenReturn(Provider.RAZORPAY);
        when(configurationService.getRequiredMap("PROVIDER_RULE", "supportedCurrencies"))
                .thenReturn(Map.of("RAZORPAY", List.of("INR")));

        RuleResult result = currencyRule.evaluate(context);

        assertEquals(RuleDecision.REJECT, result.decision());
    }
}
