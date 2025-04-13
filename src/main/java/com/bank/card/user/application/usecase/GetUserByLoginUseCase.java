package com.bank.card.user.application.usecase;

import com.bank.card.common.UseCase;
import com.bank.card.user.application.usecase.command.GetUserByEmailCommand;
import com.bank.card.user.application.usecase.dto.UserDto;
import com.bank.card.user.domain.UserModel;
import com.bank.card.user.infrastructure.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetUserByLoginUseCase {

    UserRepo repo;

    @Autowired
    public GetUserByLoginUseCase(UserRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public UserDto execute(GetUserByEmailCommand command) {
        var email = command.email();

        var user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return new UserDto(
                user.getId().uuid(),
                user.getRole(),
                user.getEmail(),
                user.getName()
        );
    }
}
