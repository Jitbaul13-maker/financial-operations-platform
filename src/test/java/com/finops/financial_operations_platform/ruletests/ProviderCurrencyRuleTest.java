package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.implementations.ProviderCurrencyRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProviderCurrencyRuleTest {
    @Mock
    TransactionContext context;

    @InjectMocks
    ProviderCurrencyRule currencyRule;

    @Test
    void shouldPassWhenProviderSupportsCurrency() {

        when(context.currency()).thenReturn("INR");
        when(context.provider()).thenReturn(Provider.WALLET);

        RuleResult result = currencyRule.evaluate(context);

        assertEquals(RuleDecision.PASS, result.decision());
    }

    @Test
    void shouldRejectWhenProviderDoesNotSupportsCurrency() {

        when(context.currency()).thenReturn("USD");
        when(context.provider()).thenReturn(Provider.RAZORPAY);

        RuleResult result = currencyRule.evaluate(context);

        assertEquals(RuleDecision.REJECT, result.decision());
    }

    @Test
    void shouldRejectWhenProviderIsUnsupported() {
        when(context.currency()).thenReturn("USD");
        when(context.provider()).thenReturn(null);

        RuleResult result = currencyRule.evaluate(context);

        assertEquals(RuleDecision.REJECT, result.decision());
    }
}
