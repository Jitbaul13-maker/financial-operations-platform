package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.RuleDecision;
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

    @InjectMocks
    VelocityRule rule;

    @Test
    void shouldPassWhenWithinLimit() {

        String customerId = "Test-123";

        when(context.customerId()).thenReturn(customerId);
        when(counterService.incrementCounter(customerId)).thenReturn(4L);

        RuleResult result = rule.evaluate(context);

        assertEquals(RuleDecision.PASS, result.decision());
    }

    @Test
    void shouldFailWhenExceedLimit() {

        String customerId = "Test-123";

        when(context.customerId()).thenReturn(customerId);
        when(counterService.incrementCounter(customerId)).thenReturn(7L);

        RuleResult result = rule.evaluate(context);

        assertEquals(RuleDecision.FLAG, result.decision());
    }
}
