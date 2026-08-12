package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleEngine;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.implementations.AmountLimitRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RuleEngineSpringTest {

    @Mock
    TransactionContext context;

    @Autowired
    RuleEngine engine;

    @Test
    void shouldWireSuccessfully(){
        RuleResult result = new RuleResult(
                "AMOUNT_RULE",
                RuleDecision.PASS,
                "Test"
        );

        when(context.amount()).thenReturn(BigDecimal.valueOf(500));

        List<RuleResult> results = engine.evaluate(context);

        for(RuleResult result1: results){
            assertEquals(result1.ruleCode(), result.ruleCode());
            assertEquals(result1.decision(), result.decision());
        }
    }
}