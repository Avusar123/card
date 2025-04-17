package com.bank.card.limit.infrastructure;

import com.bank.card.limit.domain.Limit;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.LimitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitRepo extends JpaRepository<Limit, LimitId> {
    @Query("SELECT l FROM Limit l WHERE l.id.cardId = :cardId")
    List<Limit> findAllByCardId(@Param("cardId") CardId cardId);
}
