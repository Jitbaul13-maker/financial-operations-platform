package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.rules.implementations.VelocityRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class VelocityRuleSpringTest {
    @Autowired
    VelocityRule rule;

    @Test
    void shouldWireSuccessfully(){
        assertNotNull(rule);
    }
}
