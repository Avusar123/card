package com.card_management.user.application.usecase;


import com.card_management.shared.UseCase;
import com.card_management.shared.dto.UserDto;
import com.card_management.shared.id.UserId;
import com.card_management.user.application.exception.EmailAlreadyExists;
import com.card_management.user.application.usecase.command.CreateUserCommand;
import com.card_management.user.domain.PasswordValidator;
import com.card_management.user.domain.UserModel;
import com.card_management.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDto execute(@Valid CreateUserCommand command) {
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
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getName()
        );
    }
}
