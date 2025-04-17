package com.bank.card.limit.application.usecase;

import com.bank.card.limit.application.usecase.command.DeleteLimitsByCardIdCommand;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.LimitCascade;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.id.CardId;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteLimitsByCardIdUseCase implements LimitCascade {

    LimitRepo repo;

    @Autowired
    public DeleteLimitsByCardIdUseCase(LimitRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteLimitsByCardIdCommand command) {
        var cardId = command.cardId();

        deleteAllByCardId(cardId);
    }

    @Override
    @Transactional
    public void deleteAllByCardId(CardId cardId) {
        var limits = repo.findAllByCardId(cardId);

        repo.deleteAll(limits);
    }
}
