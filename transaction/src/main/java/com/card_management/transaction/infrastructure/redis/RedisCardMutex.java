package com.card_management.transaction.infrastructure.redis;

import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisCardMutex {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisCardMutex(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean empty(CardId cardId) {
        return !redisTemplate.hasKey("card_" + cardId.uuid().toString());
    }

    public void release(CardId cardId) {
        redisTemplate.delete("card_" + cardId.uuid().toString());
    }

    public void lock(CardId cardId, TransactionId transactionId) {
        redisTemplate.opsForValue().set("card_" + cardId.uuid().toString(), transactionId.uuid().toString(), Duration.ofMinutes(60));
    }
}
