package com.finops.financial_operations_platform.testHelper;

import com.finops.financial_operations_platform.Services.IdempotencyService;
import com.finops.financial_operations_platform.Services.RedisTestService;
import com.finops.financial_operations_platform.Dtos.IdempotencyRecord;
import com.finops.financial_operations_platform.enums.IdempotencyStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTestRunner implements CommandLineRunner {

    private final RedisTestService redisTestService;
    private final RedisTemplate<String, IdempotencyRecord> idempotencyRecordRedisTemplate;
    private final IdempotencyService idempotencyService;

    public RedisTestRunner(RedisTestService redisTestService,
                           RedisTemplate<String, IdempotencyRecord> idempotencyRecordRedisTemplate,
                           IdempotencyService idempotencyService) {
        this.redisTestService = redisTestService;
        this.idempotencyRecordRedisTemplate = idempotencyRecordRedisTemplate;
        this.idempotencyService = idempotencyService;
    }

    //    @Override
//    public void run(String... args){
//        boolean result = redisTestService.testSetIfAbsent();
//        redisTestService.testSetIfAbsent();
//        System.out.println(result);
//    }

//    @Override
//    public void run(String... args){
//
//        IdempotencyRecord result = redisTestService.testSerialization();
//
//        System.out.println(result);
//    }

    @Override
    public void run(String... args){
        String key = "test1";

        IdempotencyRecord record = new IdempotencyRecord(
                IdempotencyStatus.PROCESSING,
                "test1-fingerprint",
                null
        );

        boolean first = idempotencyService.acquire(key, record);
        boolean second = idempotencyService.acquire(key, record);

        System.out.println("First: " + first);
        System.out.println("Second: " + second);

        IdempotencyRecord existing = idempotencyService.get(key);
        Long ttl = idempotencyRecordRedisTemplate.getExpire(key);

        IdempotencyRecord newRecord = idempotencyService.complete(key, "txn-123");
        Long updatedTTL = idempotencyRecordRedisTemplate.getExpire(key);

        System.out.println("existing: " + existing + "TTL: " + ttl);
        System.out.println("new Record: " + newRecord + "TTL: " + updatedTTL);
    }
}
