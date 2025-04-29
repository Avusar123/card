package com.card_management.transaction.infrastructure;


import com.card_management.shared.dto.TransactionStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import com.card_management.shared.id.UserId;
import com.card_management.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, TransactionId> {
    @Query("SELECT t FROM Transaction t WHERE completedTime >= :fromTime AND fromId = :cardId ORDER BY t.completedTime DESC")
    Page<Transaction> findOutcomeInPeriod(CardId cardId, LocalDateTime fromTime, Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE completedTime >= :fromTime AND fromId = :cardId")
    Optional<Integer> findOutcomeSumInPeriod(CardId cardId, LocalDateTime fromTime);

    @Query("SELECT t FROM Transaction t WHERE completedTime >= :fromTime AND toId = :cardId ORDER BY t.completedTime DESC")
    Page<Transaction> findIncomeInPeriod(CardId cardId, LocalDateTime fromTime, Pageable pageable);

    @Query("SELECT COUNT(*) > 0 FROM Transaction t WHERE initiator = :userId AND status = :status")
    boolean existsByStatus(UserId userId, TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE completedTime >= :fromTime AND initiator = :userId ORDER BY t.completedTime DESC")
    Page<Transaction> findByInitiatorInPeriod(UserId userId, LocalDateTime fromTime, Pageable pageable);

}
