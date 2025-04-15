package com.bank.card.card.application.usecase;

import com.bank.card.card.application.exception.CardStatusMismatch;
import com.bank.card.card.application.usecase.command.ActivateCardCommand;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.UseCase;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class ActivateCardUseCase {
    private final CardRepo repo;

    @Autowired
    public ActivateCardUseCase(CardRepo repo) {
        this.repo = repo;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid ActivateCardCommand command) {
        var card = repo.findById(command.cardId()).orElseThrow(
                () -> new EntityNotFoundException("There is no card with that Id")
        );

        if (card.getStatus() != CardStatus.BLOCKED) {
            throw new CardStatusMismatch(CardStatus.BLOCKED, card.getStatus());
        }

        card.setStatus(CardStatus.ACTIVE);

        repo.save(card);
    }
}
