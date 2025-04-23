package com.card_management.limit.application.usecase;

import com.card_management.limit.application.usecase.command.DeleteLimitByIdCommand;
import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.UseCase;
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
