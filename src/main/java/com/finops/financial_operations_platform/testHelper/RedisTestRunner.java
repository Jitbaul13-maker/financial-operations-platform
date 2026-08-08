package com.finops.financial_operations_platform.testHelper;

import com.finops.financial_operations_platform.Services.RedisTestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

public class RedisTestRunner implements CommandLineRunner {

    private final RedisTestService redisTestService;

    public RedisTestRunner(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @Override
    public void run(String... args){
        boolean result = redisTestService.testSetIfAbsent();
        redisTestService.testSetIfAbsent();
        System.out.println(result);
    }
}
