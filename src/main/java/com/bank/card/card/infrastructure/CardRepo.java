package com.bank.card.card.infrastructure;

import com.bank.card.card.domain.Card;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepo extends JpaRepository<Card, CardId> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Card c WHERE c.number.hashed = :hashed")
    boolean existsByHashedNumber(String hashed);

    Page<Card> findAllByOwnerId(UserId owner, Pageable pageable);
}
