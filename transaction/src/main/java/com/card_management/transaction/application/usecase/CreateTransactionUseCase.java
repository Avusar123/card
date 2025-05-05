package com.card_management.transaction.application.usecase;

import com.card_management.shared.UseCase;
import com.card_management.shared.dto.TransactionDto;
import com.card_management.shared.id.TransactionId;
import com.card_management.transaction.application.usecase.command.CreateTransactionCommand;
import com.card_management.transaction.domain.Transaction;
import com.card_management.transaction.infrastructure.TransactionRepo;
import com.card_management.web_security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@UseCase
public class CreateTransactionUseCase {

    private final TransactionRepo repo;

    @Autowired
    public CreateTransactionUseCase(TransactionRepo repo) {
        this.repo = repo;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionDto execute(CreateTransactionCommand command) {
        if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser user)) {
            throw new AccessDeniedException("You should re-login!");
        }

        var transaction = new Transaction(
                new TransactionId(),
                command.fromId(),
                command.toId(),
                user.getId(),
                LocalDateTime.now(),
                command.amount()
        );

        repo.save(transaction);

        return new TransactionDto(
                transaction.getId(),
                transaction.getInitiator(),
                transaction.getFromId(),
                transaction.getToId(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getCreatedTime(),
                transaction.getCompletedTime(),
                transaction.getFailureReason()
        );
    }
}
