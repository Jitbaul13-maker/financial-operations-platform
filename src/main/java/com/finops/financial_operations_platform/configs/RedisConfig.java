package com.finops.financial_operations_platform.configs;

import com.finops.financial_operations_platform.businesslogics.IdempotencyRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;


@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, IdempotencyRecord> idempotencyRedisTemplate(
            RedisConnectionFactory connectionFactory, ObjectMapper mapper) {

        RedisTemplate<String, IdempotencyRecord> template = new RedisTemplate<>();
        JacksonJsonRedisSerializer<IdempotencyRecord> serializer =
                new JacksonJsonRedisSerializer<>(mapper, IdempotencyRecord.class);

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
}
