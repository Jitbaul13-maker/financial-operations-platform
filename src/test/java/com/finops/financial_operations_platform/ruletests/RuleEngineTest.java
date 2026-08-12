package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleEngine;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.TransactionRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleEngineTest {

    @Mock
    TransactionRule rule1;

    @Mock
    TransactionRule rule2;

    @Mock
    TransactionContext context;

    @Test
    void shouldEvaluateAllRulesAndReturnAllResults() {

        RuleResult result1 = new RuleResult(
                "ABC",
                RuleDecision.PASS,
                "Testing"
        );

        RuleResult result2 = new RuleResult(
                "XYZ",
                RuleDecision.REJECT,
                "Testing"
        );

        RuleEngine engine = new RuleEngine(List.of(rule1, rule2));

        when(rule1.evaluate(context)).thenReturn(result1);
        when(rule2.evaluate(context)).thenReturn(result2);

        List<RuleResult> results = engine.evaluate(context);

        assertEquals(List.of(result1, result2), results);
    }
}
