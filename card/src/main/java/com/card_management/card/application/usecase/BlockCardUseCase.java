package com.card_management.card.application.usecase;

import com.card_management.card.application.exception.CardStatusMismatch;
import com.card_management.card.application.usecase.command.BlockCardCommand;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.dto.CardDto;
import com.card_management.shared.dto.CardStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class BlockCardUseCase {
    private final CardRepo repo;

    @Autowired
    public BlockCardUseCase(CardRepo repo) {
        this.repo = repo;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostAuthorize("returnObject.ownerId == authentication.principal.id || hasAuthority('ADMIN')")
    public CardDto execute(@Valid BlockCardCommand command) {
        var card = repo.findById(command.cardId()).orElseThrow(
                () -> new EntityNotFoundException("There is no card with that Id")
        );

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardStatusMismatch(CardStatus.ACTIVE, card.getStatus());
        }

        card.setStatus(CardStatus.BLOCKED);

        repo.save(card);

        return new CardDto(
                card.getId(),
                card.getOwnerId(),
                card.getNumber().mask(),
                card.getStatus(),
                card.getCardValue(),
                card.getExpires()
        );
    }
}
