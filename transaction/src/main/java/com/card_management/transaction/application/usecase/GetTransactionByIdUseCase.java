package com.card_management.transaction.application.usecase;

import com.card_management.shared.UseCase;
import com.card_management.shared.dto.TransactionDto;
import com.card_management.transaction.application.usecase.command.GetTransactionByIdCommand;
import com.card_management.transaction.infrastructure.TransactionRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetTransactionByIdUseCase {

    private final TransactionRepo repo;

    @Autowired
    public GetTransactionByIdUseCase(TransactionRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasAuthority('ADMIN') || authentication.principal.id == returnObject.initiator")
    public TransactionDto execute(@Valid GetTransactionByIdCommand command) {
        var id = command.id();

        var transaction = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found!"));

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
