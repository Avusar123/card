package com.card_management.transaction.infrastructure.saga.listener;

import com.card_management.shared.dto.TransactionStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import com.card_management.transaction.infrastructure.TransactionRepo;
import com.card_management.transaction.infrastructure.redis.RedisCardMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class TransactionSagaFinisher {

    private final TransactionRepo repo;
    private final RedisCardMutex mutex;

    @Autowired
    public TransactionSagaFinisher(TransactionRepo repo,
                                   RedisCardMutex mutex) {
        this.repo = repo;
        this.mutex = mutex;
    }

    public void success(TransactionId transactionId) {
        var transaction = repo.findById(transactionId).orElseThrow();

        transaction.setStatus(TransactionStatus.COMPLETED);

        transaction.setCompletedTime(LocalDateTime.now());

        mutex.release(transaction.getFromId());

        mutex.release(transaction.getToId());
    }

    public void forceRelease(CardId from, CardId to) {
        mutex.release(from);

        mutex.release(to);
    }

    public void failure(TransactionId transactionId, String failureMessage) {
        var transaction = repo.findById(transactionId).orElseThrow();

        transaction.setFailureReason(failureMessage);

        transaction.setStatus(TransactionStatus.FAILED);

        repo.save(transaction);

        mutex.release(transaction.getFromId());

        mutex.release(transaction.getToId());
    }
}
