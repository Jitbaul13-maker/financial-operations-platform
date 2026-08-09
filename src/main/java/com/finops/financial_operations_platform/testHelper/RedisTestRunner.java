package com.finops.financial_operations_platform.testHelper;

import com.finops.financial_operations_platform.Services.RedisTestService;
import com.finops.financial_operations_platform.businesslogics.IdempotencyRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

public class RedisTestRunner implements CommandLineRunner {

    private final RedisTestService redisTestService;
    private final RedisTemplate<String, IdempotencyRecord> idempotencyRecordRedisTemplate;

    public RedisTestRunner(RedisTestService redisTestService, RedisTemplate<String, IdempotencyRecord> idempotencyRecordRedisTemplate) {
        this.redisTestService = redisTestService;
        this.idempotencyRecordRedisTemplate = idempotencyRecordRedisTemplate;
    }

    //    @Override
//    public void run(String... args){
//        boolean result = redisTestService.testSetIfAbsent();
//        redisTestService.testSetIfAbsent();
//        System.out.println(result);
//    }

    @Override
    public void run(String... args){

        IdempotencyRecord result = redisTestService.testSerialization();

        System.out.println(result);
    }
}
