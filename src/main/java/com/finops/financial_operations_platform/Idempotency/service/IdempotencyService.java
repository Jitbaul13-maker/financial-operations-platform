package com.finops.financial_operations_platform.Idempotency.service;

import com.finops.financial_operations_platform.Exceptions.IdempotencyStateException;
import com.finops.financial_operations_platform.Idempotency.dto.IdempotencyRecord;
import com.finops.financial_operations_platform.Idempotency.dto.IdempotencyResult;
import com.finops.financial_operations_platform.Idempotency.enums.IdempotencyDecision;
import com.finops.financial_operations_platform.Idempotency.enums.IdempotencyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final RedisTemplate<String, IdempotencyRecord> redisTemplate;
    private final RedisScript<Long> completeIdempotencyScript;

    private Boolean acquire(String key, IdempotencyRecord record) {
        return redisTemplate.opsForValue().setIfAbsent(key, record, Duration.ofMinutes(2));
    }

    private IdempotencyResult resultBuilder(IdempotencyRecord existingRecord, String fingerprint){

        if (Objects.equals(existingRecord.requestFingerprint(), fingerprint))
        {
            switch (existingRecord.status()){
                case COMPLETED: return new IdempotencyResult(
                        existingRecord,
                        IdempotencyDecision.COMPLETED
                );

                case PROCESSING:
                    return new IdempotencyResult(
                            existingRecord,
                            IdempotencyDecision.IN_PROGRESS
                    );
            }
        }
        else return new IdempotencyResult(
                null,
                IdempotencyDecision.CONFLICT
        );

        throw new IdempotencyStateException("Idempotency failure");
    }

    private IdempotencyRecord get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void complete(String key, String txnId) {

        Long result = redisTemplate.execute(
                completeIdempotencyScript,
                List.of(key),
                txnId
        );

        if (result == null) {
            throw new IllegalStateException("Unexpected null result from Redis");
        }

        switch (result.intValue()) {
            case 0 -> throw new IdempotencyStateException("No valid record found!");
            case 1 -> throw new IdempotencyStateException("Invalid record state!");
            case 2 -> { return; }
            default -> throw new IllegalStateException("Unexpected Redis result: " + result);
        }
    }

    public IdempotencyResult claim(String key, String fingerprint) {

        IdempotencyRecord record = new IdempotencyRecord(
                IdempotencyStatus.PROCESSING,
                fingerprint,
                null
        );

        if(acquire(key, record)) {
            return new IdempotencyResult(
                    record,
                    IdempotencyDecision.ACQUIRED
            );
        }
        else {
            IdempotencyRecord existingRecord = get(key);

            if (existingRecord == null) {
                if(acquire(key, record)) {
                    return new IdempotencyResult(
                            record,
                            IdempotencyDecision.ACQUIRED
                    );
                } else {
                    IdempotencyRecord existingRecord1 = get(key);
                    if (existingRecord1 == null) {
                        throw new IdempotencyStateException("Idempotency failure");
                    }
                    return resultBuilder(existingRecord1, fingerprint);
                }
            }
            else{
                return resultBuilder(existingRecord, fingerprint);
            }
        }
    }
}
