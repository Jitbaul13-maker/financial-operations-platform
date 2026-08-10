package com.finops.financial_operations_platform.ruleTests;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.implementations.AmountLimitRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class AmountLimitRuleTest {

    @Mock
    TransactionContext context;

    @InjectMocks
    AmountLimitRule amountLimitRule;

    @Test
    void shouldPassWhenAmountIsBelowLimit(){

        BigDecimal amount = BigDecimal.valueOf(5000);
        when(context.amount()).thenReturn(amount);

        RuleResult result = amountLimitRule.evaluate(context);

        assertEquals(RuleDecision.PASS, result.decision());
    }

    @Test
    void shouldFailWhenAmountIsAboveLimit() {
        BigDecimal amount = BigDecimal.valueOf(500000);
        when(context.amount()).thenReturn(amount);

        RuleResult result = amountLimitRule.evaluate(context);

        assertEquals(RuleDecision.REJECT, result.decision());
    }
}
