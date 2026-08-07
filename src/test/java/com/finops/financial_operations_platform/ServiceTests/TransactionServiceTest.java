package com.finops.financial_operations_platform.ServiceTests;

import com.finops.financial_operations_platform.Dtos.TransactionResponse;
import com.finops.financial_operations_platform.Services.AuditLogService;
import com.finops.financial_operations_platform.Services.TransactionService;
import com.finops.financial_operations_platform.businesslogics.TransactionStateMachine;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import com.finops.financial_operations_platform.models.Transaction;
import com.finops.financial_operations_platform.repos.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.finops.financial_operations_platform.enums.TransactionStatus.INITIATED;
import static com.finops.financial_operations_platform.enums.TransactionStatus.PROCESSING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AuditLogService auditLogService;

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
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);

        TransactionResponse response = service.updateTransactionStatus("TXN-123", PROCESSING);

        assertEquals(PROCESSING, response.status());
        assertEquals(PROCESSING, tx.getStatus());
        assertEquals(tx.getTransactionId(), response.transactionId());

        verify(transactionStateMachine).validateTransition(INITIATED, PROCESSING);

//        verify(transactionRepository).save(tx);

    }
}
