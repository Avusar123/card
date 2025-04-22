package com.card_management.card.application.usecase;

import com.card_management.card.application.LimitCascade;
import com.card_management.card.application.usecase.command.DeleteCardByIdCommand;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.UseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteCardByIdUseCase {

    private final LimitCascade cascade;
    CardRepo repo;

    @Autowired
    public DeleteCardByIdUseCase(CardRepo repo,
                                 LimitCascade cascade) {
        this.repo = repo;
        this.cascade = cascade;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteCardByIdCommand command) {
        var id = command.id();

        repo.deleteById(id);

        cascade.deleteAllByCardId(id);
    }
}
