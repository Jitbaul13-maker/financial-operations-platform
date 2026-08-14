package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.implementations.DuplicateRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DuplicateRuleSpringTest {

    @Mock
    TransactionContext context;

    @Autowired
    DuplicateRule rule;

    @Test
    void ShouldWireSuccessfully(){
        assertNotNull(rule);
    }
}
