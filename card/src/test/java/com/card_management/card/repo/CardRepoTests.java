package com.card_management.card.repo;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.domain.Card;
import com.card_management.card.domain.CardNumber;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.dto.CardStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@DataJpaTest
public class CardRepoTests {
    @Autowired
    private CardRepo cardRepo;


    @MockitoBean
    private CardNumberEncoder encoder;

    @BeforeEach
    void clearRepo() {
        cardRepo.deleteAll();
    }

    @Test
    void findByOwnerId_success_valid() {
        var userId = new UserId();

        var cardId = new CardId();

        var card = new Card(cardId,
                userId,
                new CardNumber("Mask", "Hash"),
                CardStatus.ACTIVE,
                0,
                LocalDate.now());

        cardRepo.save(card);

        var result = cardRepo.findAllByOwnerId(userId, PageRequest.of(0, 1));

        Assertions.assertEquals(1, result.getNumberOfElements());
    }

    @Test
    @Transactional
    void existsByHashedNumber_success_validHash() {
        var password = "pass";

        Mockito.when(encoder.encode(password)).thenReturn(new CardNumber("Mask", "Hashed"));

        Mockito.when(encoder.decode("Hashed")).thenReturn(password);

        var userId = new UserId();

        var cardId = new CardId();

        var encoded = encoder.encode(password);

        var card = new Card(cardId,
                userId,
                new CardNumber("Mask", encoded.hashed()),
                CardStatus.ACTIVE,
                0,
                LocalDate.now());

        cardRepo.save(card);

        Assertions.assertTrue(cardRepo.existsByHashedNumber(encoded.hashed()));
    }


}
