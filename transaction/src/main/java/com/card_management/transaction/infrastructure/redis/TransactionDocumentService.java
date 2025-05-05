package com.card_management.transaction.infrastructure.redis;

import com.card_management.shared.id.TransactionId;
import com.card_management.transaction.infrastructure.saga.TransactionDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionDocumentService {

    private final RedisTemplate<String, TransactionDocument> redisTemplate;

    @Autowired
    public TransactionDocumentService(RedisTemplate<String, TransactionDocument> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(TransactionDocument document) {
        redisTemplate.opsForValue().set(generateKey(document.getId()), document);
    }

    public TransactionDocument get(TransactionId id) {
        return redisTemplate.opsForValue().get(generateKey(id));
    }

    public boolean exists(TransactionId id) {
        return redisTemplate.hasKey(generateKey(id));
    }

    public void delete(TransactionId id) {
        redisTemplate.delete(generateKey(id));
    }

    private String generateKey(TransactionId id) {
        return "transaction:" + id.uuid().toString();
    }
}
