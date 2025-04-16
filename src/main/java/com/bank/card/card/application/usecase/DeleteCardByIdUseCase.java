package com.bank.card.card.application.usecase;

import com.bank.card.card.application.usecase.command.DeleteCardByIdCommand;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.UseCase;
import com.bank.card.user.application.usecase.command.DeleteUserByEmailCommand;
import com.bank.card.user.infrastructure.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteCardByIdUseCase {

    CardRepo repo;

    @Autowired
    public DeleteCardByIdUseCase(CardRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteCardByIdCommand command) {
        var id = command.id();

        repo.deleteById(id);
    }
}
