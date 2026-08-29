package com.finops.financial_operations_platform.ServiceTests;

import com.finops.financial_operations_platform.Dtos.CreateTransactionRequest;
import com.finops.financial_operations_platform.Dtos.IdempotencyRecord;
import com.finops.financial_operations_platform.Dtos.IdempotencyResult;
import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Exceptions.TransactionRuleRejectedException;
import com.finops.financial_operations_platform.audit.service.AuditLogService;
import com.finops.financial_operations_platform.Services.IdempotencyService;
import com.finops.financial_operations_platform.Services.RequestFingerprintService;
import com.finops.financial_operations_platform.Services.TransactionService;
import com.finops.financial_operations_platform.businesslogics.TransactionStateMachine;
import com.finops.financial_operations_platform.enums.*;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import com.finops.financial_operations_platform.rules.RuleEngine;
import com.finops.financial_operations_platform.rules.RuleResult;
import com.finops.financial_operations_platform.rules.TransactionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.finops.financial_operations_platform.enums.TransactionStatus.INITIATED;
import static com.finops.financial_operations_platform.enums.TransactionStatus.PROCESSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AuditLogService auditLogService;

    @Mock
    RuleEngine ruleEngine;

    @Mock
    RequestFingerprintService fingerprintService;

    @Mock
    IdempotencyService idempotencyService;

    @Mock
    TransactionStateMachine transactionStateMachine;

    @InjectMocks
    TransactionService service;

    @Test
    void shouldUpdateSuccessfully(){

        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-123");
        tx.setStatus(TransactionStatus.INITIATED);

        when(transactionRepository.findByTransactionId("TXN-123")).thenReturn(Optional.of(tx));

        TransactionResponse response = service.updateTransactionStatus("TXN-123", PROCESSING);

        assertEquals(PROCESSING, response.status());
        assertEquals(PROCESSING, tx.getStatus());
        assertEquals(tx.getTransactionId(), response.transactionId());

        verify(transactionStateMachine).validateTransition(INITIATED, PROCESSING);

    }

    @Test
    void shouldRejectTransactionCreation() {

        CreateTransactionRequest request = new CreateTransactionRequest(
                Provider.RAZORPAY,
                "PROVIDER-TEST-123",
                BigDecimal.valueOf(500),
                "INR",
                "CUSTOMER-TEST-123"
        );

        IdempotencyRecord record = new IdempotencyRecord(
                IdempotencyStatus.PROCESSING,
                "abc",
                "TXN-TEST-123"
        );

        RuleResult ruleResult1 = new RuleResult("Test", RuleDecision.REJECT, "Test");
        IdempotencyResult idempotencyResult = new IdempotencyResult(record, IdempotencyDecision.ACQUIRED);

        when(fingerprintService.generate(request)).thenReturn("abc");
        when(idempotencyService.claim("Test", "abc")).thenReturn(idempotencyResult);
        when(ruleEngine.evaluate(any(TransactionContext.class))).thenReturn(List.of(ruleResult1));

        assertThrows(TransactionRuleRejectedException.class, () -> service.createTransaction(request, "Test"));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldPassTransactionCreation() {

        CreateTransactionRequest request = new CreateTransactionRequest(
                Provider.RAZORPAY,
                "PROVIDER-TEST-123",
                BigDecimal.valueOf(500),
                "INR",
                "CUSTOMER-TEST-123"
        );

        IdempotencyRecord record = new IdempotencyRecord(
                IdempotencyStatus.PROCESSING,
                "abc",
                "TXN-TEST-123"
        );

        Transaction savedTx = new Transaction();
        savedTx.setTransactionId("TXN-TEST-123");

        RuleResult ruleResult1 = new RuleResult("Test", RuleDecision.PASS, "Test");
        IdempotencyResult idempotencyResult = new IdempotencyResult(record, IdempotencyDecision.ACQUIRED);

        when(fingerprintService.generate(request)).thenReturn("abc");
        when(idempotencyService.claim("Test", "abc")).thenReturn(idempotencyResult);
        when(ruleEngine.evaluate(any(TransactionContext.class))).thenReturn(List.of(ruleResult1));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTx);

        assertDoesNotThrow(() -> service.createTransaction(request, "Test"));
        verify(transactionRepository).save(any(Transaction.class));
    }
}