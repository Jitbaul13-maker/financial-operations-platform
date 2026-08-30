package com.finops.financial_operations_platform.Services;

import com.finops.financial_operations_platform.Dtos.IdempotencyResult;
import com.finops.financial_operations_platform.Exceptions.IdempotencyStateException;
import com.finops.financial_operations_platform.Dtos.IdempotencyRecord;
import com.finops.financial_operations_platform.enums.IdempotencyDecision;
import com.finops.financial_operations_platform.enums.IdempotencyStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
public class IdempotencyService {

    private final RedisTemplate<String, IdempotencyRecord> redisTemplate;

    public IdempotencyService(RedisTemplate<String, IdempotencyRecord> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
        IdempotencyRecord record = redisTemplate.opsForValue().get(key);

        if(record == null) throw new IdempotencyStateException("No valid record found!");

        if (record.status() == IdempotencyStatus.PROCESSING) {
            IdempotencyRecord newRecord = new IdempotencyRecord(
                    IdempotencyStatus.COMPLETED,
                    record.requestFingerprint(),
                    txnId
            );

            redisTemplate.opsForValue().set(
                    key,
                    newRecord,
                    Duration.ofHours(24)
            );
            return;
        }

        throw new IdempotencyStateException("Invalid record state!");
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
