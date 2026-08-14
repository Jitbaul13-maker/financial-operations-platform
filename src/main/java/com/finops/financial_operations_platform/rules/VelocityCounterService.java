package com.finops.financial_operations_platform.rules;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class VelocityCounterService {

    private final RedisTemplate<String, String> details;

    public VelocityCounterService(RedisTemplate<String, String> details) {
        this.details = details;
    }

    public long incrementCounter(String customerId) {

        long count = details.opsForValue().increment(customerId);

        if (count == 1) {
            details.expire(customerId, Duration.ofSeconds(60));
        }

        return count;
    }
}
