package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.Provider;
import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.rules.RuleEngine;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.VelocityCounterService;
import com.finops.financial_operations_platform.rules.implementations.AmountLimitRule;
import com.finops.financial_operations_platform.rules.implementations.VelocityRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RuleEngineSpringTest {

    @Mock
    TransactionContext context;

    @MockitoBean
    VelocityCounterService counterService;

    @Autowired
    RuleEngine engine;

    @Test
    void shouldWireSuccessfully(){
        RuleResult result = new RuleResult(
                "RULE",
                RuleDecision.PASS,
                "Test"
        );

        when(counterService.incrementCounter("Test-123", 1L)).thenReturn(4L);

        when(context.amount()).thenReturn(BigDecimal.valueOf(500));
        when(context.currency()).thenReturn("INR");
        when(context.provider()).thenReturn(Provider.RAZORPAY);
        when(context.customerId()).thenReturn("Test-123");

        List<RuleResult> results = engine.evaluate(context);

        for(RuleResult result1: results){
            assertEquals(result1.decision(), result.decision());
        }
    }
}