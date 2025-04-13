package com.bank.card.user.application.usecase;

import com.bank.card.common.UseCase;
import com.bank.card.user.application.usecase.command.DeleteUserByEmailCommand;
import com.bank.card.user.infrastructure.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteUserByEmailUseCase {

    UserRepo repo;

    @Autowired
    public DeleteUserByEmailUseCase(UserRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(DeleteUserByEmailCommand command) {
        var email = command.email();

        var user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found!"));

        repo.delete(user);
    }
}
