package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleConfigurationService;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.VelocityCounterService;
import com.finops.financial_operations_platform.rules.implementations.VelocityRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VelocityRuleTest {

    @Mock
    TransactionContext context;

    @Mock
    VelocityCounterService counterService;

    @Mock
    RuleConfigurationService configurationService;

    @InjectMocks
    VelocityRule rule;

    @Test
    void shouldPassWhenWithinLimit() {

        String customerId = "Test-123";

        when(context.customerId()).thenReturn(customerId);
        when(configurationService.getRequiredLong("VELOCITY_RULE", "windowMinutes"))
                .thenReturn(2L);
        when(counterService.incrementCounter(customerId, 2L)).thenReturn(4L);
        when(configurationService.getRequiredLong("VELOCITY_RULE", "maxTransactions"))
                .thenReturn(5L);

        RuleResult result = rule.evaluate(context);

        assertEquals(RuleDecision.PASS, result.decision());
    }

    @Test
    void shouldFailWhenExceedLimit() {

        String customerId = "Test-123";

        when(context.customerId()).thenReturn(customerId);
        when(configurationService.getRequiredLong("VELOCITY_RULE", "windowMinutes"))
                .thenReturn(2L);
        when(counterService.incrementCounter(customerId, 2L)).thenReturn(7L);
        when(configurationService.getRequiredLong("VELOCITY_RULE", "maxTransactions"))
                .thenReturn(5L);

        RuleResult result = rule.evaluate(context);

        assertEquals(RuleDecision.FLAG, result.decision());
    }
}
