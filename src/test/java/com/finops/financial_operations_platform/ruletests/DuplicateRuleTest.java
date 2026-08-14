package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.enums.RuleDecision;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import com.finops.financial_operations_platform.rules.implementations.DuplicateRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DuplicateRuleTest {

    @Mock
    TransactionRepository repository;

    @Mock
    TransactionContext context;

    @InjectMocks
    DuplicateRule rule;

    @Test
    void shouldRejectWhenTransactionAlreadyExists() {

        String providerTransactionId = "Test-123";

        when(context.providerTransactionId()).thenReturn(providerTransactionId);
        when(repository.existsByProviderTransactionId(context.providerTransactionId())).thenReturn(true);

        RuleResult result = rule.evaluate(context);

        verify(repository).existsByProviderTransactionId(providerTransactionId);
        assertEquals(RuleDecision.REJECT, result.decision());
    }

    @Test
    void shouldPassWhenTransactionDoesNotExist() {

        String providerTransactionId = "Test-123";

        when(context.providerTransactionId()).thenReturn(providerTransactionId);
        when(repository.existsByProviderTransactionId(context.providerTransactionId())).thenReturn(false);

        RuleResult result = rule.evaluate(context);

        verify(repository).existsByProviderTransactionId(providerTransactionId);
        assertEquals(RuleDecision.PASS, result.decision());
    }
}
