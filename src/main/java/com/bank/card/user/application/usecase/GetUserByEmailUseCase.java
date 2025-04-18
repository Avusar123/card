package com.bank.card.user.application.usecase;

import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.UserDto;
import com.bank.card.user.application.usecase.command.GetUserByEmailCommand;
import com.bank.card.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetUserByEmailUseCase {

    UserRepo repo;

    @Autowired
    public GetUserByEmailUseCase(UserRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN') || authentication.name == #command.email")
    public UserDto execute(@Valid GetUserByEmailCommand command) {
        var email = command.email();

        var user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return new UserDto(
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getName()
        );
    }
}
