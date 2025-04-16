package com.bank.card.card.application.usecase;

import com.bank.card.card.application.exception.CardStatusMismatch;
import com.bank.card.card.application.usecase.command.BlockCardCommand;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.CardDto;
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
                card.getOwner().getName(),
                card.getOwner().getId(),
                card.getNumber().mask(),
                card.getStatus(),
                card.getCardValue(),
                card.getExpires()
        );
    }
}
