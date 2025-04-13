package com.bank.card.user.application.usecase;

import com.bank.card.common.UseCase;
import com.bank.card.user.application.exception.EmailAlreadyExists;
import com.bank.card.user.application.usecase.command.CreateUserCommand;
import com.bank.card.user.application.usecase.command.DeleteUserByEmailCommand;
import com.bank.card.user.application.usecase.dto.UserDto;
import com.bank.card.user.domain.PasswordValidator;
import com.bank.card.user.domain.UserId;
import com.bank.card.user.domain.UserModel;
import com.bank.card.user.infrastructure.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class CreateUserUseCase {

    UserRepo repo;
    PasswordEncoder passwordEncoder;

    @Autowired
    public CreateUserUseCase(UserRepo repo, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
    }

    @Transactional
    public UserDto execute(CreateUserCommand command) {
        var email = command.email();

        if (repo.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExists(email);
        }

        PasswordValidator.throwIfInvalid(command.password());

        var user = new UserModel(new UserId(),
                command.role(),
                command.email(),
                passwordEncoder.encode(command.password()),
                command.name());

        user = repo.save(user);

        return new UserDto(
                user.getId().uuid(),
                user.getRole(),
                user.getEmail(),
                user.getName()
        );
    }
}
