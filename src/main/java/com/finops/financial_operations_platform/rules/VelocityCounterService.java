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

    String ruleCode = "VELOCITY_RULE";

    public long incrementCounter(String customerId, Long windowMinutes) {

        long count = details.opsForValue().increment(customerId);

        if (count == 1) {
            details.expire(customerId, Duration.ofMinutes(windowMinutes));
        }

        return count;
    }
}
