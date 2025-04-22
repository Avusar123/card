package com.bank.card.user.application.usecase;

import com.bank.card.shared.UseCase;
import com.bank.card.shared.api.UserProvider;
import com.bank.card.shared.dto.UserDto;
import com.bank.card.shared.id.UserId;
import com.bank.card.user.application.usecase.command.GetUserByIdCommand;
import com.bank.card.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetUserByIdUseCase implements UserProvider {
    UserRepo repo;

    @Autowired
    public GetUserByIdUseCase(UserRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN') || authentication.principal.id == #command.id")
    public UserDto execute(@Valid GetUserByIdCommand command) {
        var id = command.id();

        return getById(id);
    }

    @Override
    public UserDto getById(UserId id) {
        var user = repo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return new UserDto(
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getName()
        );
    }

    @Override
    public boolean exist(UserId id) {
        return repo.existsById(id);
    }
}
