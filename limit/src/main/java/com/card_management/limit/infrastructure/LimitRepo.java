package com.card_management.limit.infrastructure;

import com.card_management.limit.domain.Limit;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitRepo extends JpaRepository<Limit, LimitId> {
    @Query("SELECT l FROM Limit l WHERE l.id.cardId = :cardId")
    List<Limit> findAllByCardId(@Param("cardId") CardId cardId);

    @Modifying
    @Query("DELETE FROM Limit l WHERE l.id.cardId = :cardId")
    void deleteAllByCardId(@Param("cardId") CardId cardId);
}
