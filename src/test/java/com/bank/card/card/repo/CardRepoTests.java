package com.bank.card.card.repo;

import com.bank.card.card.domain.Card;
import com.bank.card.card.domain.CardNumber;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.api.CardNumberEncoder;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import com.bank.card.user.domain.UserModel;
import com.bank.card.user.domain.UserRole;
import com.bank.card.user.infrastructure.UserRepo;
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

    @Autowired
    private UserRepo userRepo;

    @MockitoBean
    private CardNumberEncoder encoder;

    @BeforeEach
    void clearRepo() {
        cardRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void findByOwnerId_success_valid() {
        var userId = new UserId();

        var cardId = new CardId();

        var userModel = new UserModel(userId, UserRole.CLIENT,
                "test@gmail.com",
                "Encoded",
                "Test");

        var card = new Card(cardId,
                userModel,
                new CardNumber("Mask", "Hash"),
                CardStatus.ACTIVE,
                0,
                LocalDate.now());

        userModel = userRepo.save(userModel);

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

        var userModel = new UserModel(userId, UserRole.CLIENT,
                "test@gmail.com",
                "Encoded",
                "Test");

        var card = new Card(cardId,
                userModel,
                new CardNumber("Mask", encoded.hashed()),
                CardStatus.ACTIVE,
                0,
                LocalDate.now());

        userModel = userRepo.save(userModel);

        cardRepo.save(card);

        Assertions.assertTrue(cardRepo.existsByHashedNumber(encoded.hashed()));
    }


}
