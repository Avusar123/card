package com.bank.card.limit.application.usecase;

import com.bank.card.limit.application.usecase.command.DeleteLimitByIdCommand;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.UseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteLimitByIdUseCase {

    LimitRepo repo;

    @Autowired
    public DeleteLimitByIdUseCase(LimitRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteLimitByIdCommand command) {
        var id = command.limitId();

        repo.deleteById(id);
    }
}
