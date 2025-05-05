package com.card_management.shared.redis;

import com.card_management.shared.id.TransactionId;
import com.card_management.shared.kafka.event.TransactionSagaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ResponseCacheService {

    private final RedisTemplate<String, TransactionSagaResponse> redisTemplate;

    @Autowired
    public ResponseCacheService(RedisTemplate<String, TransactionSagaResponse> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setCache(String serviceName, TransactionId id, TransactionSagaResponse response) {
        redisTemplate.opsForValue().set(generateKey(serviceName, id), response, Duration.ofMinutes(60));
    }

    public TransactionSagaResponse get(String serviceName, TransactionId id) {
        return redisTemplate.opsForValue().get(generateKey(serviceName, id));
    }

    public boolean cached(String serviceName, TransactionId id) {
        return redisTemplate.hasKey(generateKey(serviceName, id));
    }

    private String generateKey(String serviceName, TransactionId id) {
        return serviceName + ":" + id.uuid().toString();
    }
}
