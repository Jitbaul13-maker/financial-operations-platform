package com.finops.financial_operations_platform.ruletests;

import com.finops.financial_operations_platform.rules.VelocityCounterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VelocityCounterTest {

    @Mock
    RedisTemplate<String, String> details;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    VelocityCounterService service;

    @Test
    void shouldStartCounterAndSetExpiryForFirstTransaction() {

        when(details.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("Test-123")).thenReturn(1L);

        Long count = service.incrementCounter("Test-123");

        assertEquals(1, count);
        verify(valueOperations).increment("Test-123");
        verify(details).expire("Test-123", Duration.ofSeconds(60));
    }

    @Test
    void shouldIncrementCounterForNonFirstTransaction() {
        when(details.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("Test-123")).thenReturn(2L);

        Long count = service.incrementCounter("Test-123");

        assertEquals(2, count);
        verify(valueOperations).increment("Test-123");
        verify(details, never()).expire("Test-123", Duration.ofSeconds(60));
    }
}
