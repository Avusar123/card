package com.card_management.user.application.usecase;

import com.card_management.shared.UseCase;
import com.card_management.shared.kafka.event.UserDeletedEvent;
import com.card_management.user.application.UserDeletedProducer;
import com.card_management.user.application.usecase.command.DeleteUserByEmailCommand;
import com.card_management.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteUserByEmailUseCase {

    UserRepo repo;

    UserDeletedProducer producer;

    @Autowired
    public DeleteUserByEmailUseCase(UserRepo repo,
                                    UserDeletedProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteUserByEmailCommand command) {
        var email = command.email();

        var user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found!"));

        producer.send(new UserDeletedEvent(user.getId()));

        repo.delete(user);
    }
}
