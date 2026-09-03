package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Idempotency.dto.IdempotencyRecord;
import com.finops.financial_operations_platform.Idempotency.enums.IdempotencyStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Service
public class RedisTestService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, IdempotencyRecord> idempotencyRecordRedisTemplate;

    public RedisTestService(RedisTemplate<String, String> redisTemplate,
                            RedisTemplate<String, IdempotencyRecord> idempotencyRecordRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.idempotencyRecordRedisTemplate = idempotencyRecordRedisTemplate;
    }

    public void testRedis() {
        redisTemplate.opsForValue().set("spring:test", "hello-from-spring");
    }

    public boolean testSetIfAbsent() {
        return redisTemplate.opsForValue()
                .setIfAbsent("redis:nx-test", "request-1", Duration.ofSeconds(30));
    }

    public IdempotencyRecord testSerialization() {
        IdempotencyRecord record = new IdempotencyRecord(
                IdempotencyStatus.PROCESSING,
                "test-fingerprint",
                null
        );

        String key = "idempotency:serialization-test";

        idempotencyRecordRedisTemplate.opsForValue().set(key, record);
        System.out.println("SET completed");
        System.out.println("Exists: " + idempotencyRecordRedisTemplate.hasKey(key));

        IdempotencyRecord result =
                idempotencyRecordRedisTemplate.opsForValue().get("idempotency:serialization-test");

        return record;
    }
}
