package com.bank.card.transaction.infrastructure;

import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.TransactionId;
import com.bank.card.shared.id.UserId;
import com.bank.card.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, TransactionId> {
    @Query("SELECT t FROM Transaction t WHERE time >= :fromTime AND fromId = :cardId ORDER BY t.time DESC")
    Page<Transaction> findOutcomeInPeriod(CardId cardId, LocalDateTime fromTime, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE time >= :fromTime AND toId = :cardId ORDER BY t.time DESC")
    Page<Transaction> findIncomeInPeriod(CardId cardId, LocalDateTime fromTime, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE time >= :fromTime AND initiator = :userId ORDER BY t.time DESC")
    Page<Transaction> findByInitiatorInPeriod(UserId userId, LocalDateTime fromTime, Pageable pageable);

}
