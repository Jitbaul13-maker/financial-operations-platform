package com.finops.financial_operations_platform.ServiceTests;

import com.finops.financial_operations_platform.Exceptions.IdempotencyStateException;
import com.finops.financial_operations_platform.Services.IdempotencyService;
import com.finops.financial_operations_platform.businesslogics.TransactionStateMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class IdempotencyServiceTest {

    @Autowired
    private IdempotencyService service;

    @Test
    void shouldNotCompleteAlreadyCompletedRecord() {
        String key = "test-key-1";
        String id = "TX-123";

        service.claim(key, "fp");
        service.complete(key, id);

        assertThrows(
                IdempotencyStateException.class,
                () -> service.complete(key, id)
        );
    }
}
