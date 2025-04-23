package com.card_management.limit.application.usecase;

import com.card_management.limit.application.usecase.command.DeleteLimitsByCardIdCommand;
import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.UseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteLimitsByCardIdUseCase {

    LimitRepo repo;

    @Autowired
    public DeleteLimitsByCardIdUseCase(LimitRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteLimitsByCardIdCommand command) {
        var cardId = command.cardId();

        var limits = repo.findAllByCardId(cardId);

        repo.deleteAll(limits);
    }
}
