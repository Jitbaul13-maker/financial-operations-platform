package com.finops.financial_operations_platform.Services;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public class RedisTestService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTestService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testRedis() {
        redisTemplate.opsForValue().set("spring:test", "hello-from-spring");
    }

    public boolean testSetIfAbsent() {
        return redisTemplate.opsForValue()
                .setIfAbsent("redis:nx-test", "request-1", Duration.ofSeconds(30));
    }
}
