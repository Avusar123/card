package com.card_management.transaction.application.usecase;

import com.card_management.shared.UseCase;
import com.card_management.shared.dto.TransactionDto;
import com.card_management.shared.dto.UserRole;
import com.card_management.transaction.application.usecase.command.GetUserTransactionsByRangeCommand;
import com.card_management.transaction.domain.Transaction;
import com.card_management.transaction.infrastructure.TransactionRepo;
import com.card_management.web_security.SecurityUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@UseCase
public class GetUserTransactionsByRangeUseCase {
    private final TransactionRepo repo;

    @Autowired
    public GetUserTransactionsByRangeUseCase(TransactionRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> execute(@Valid GetUserTransactionsByRangeCommand command) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser user
                && !(user.getRole() == UserRole.ADMIN || user.getId().equals(command.userId()))) {
            throw new AccessDeniedException("You do not have access to see this user's transaction history!");
        }

        PageRequest pageRequest = PageRequest.of(command.page(), command.pageSize());

        List<Transaction> transactions = repo.findByInitiatorInPeriod(
                command.userId(),
                LocalDateTime.now().minusDays(command.range().getDays()),
                pageRequest
        ).getContent();

        return transactions.stream()
                .map(t ->
                        new TransactionDto(
                                t.getId(),
                                t.getInitiator(),
                                t.getFromId(),
                                t.getToId(),
                                t.getAmount(),
                                t.getStatus(),
                                t.getCreatedTime(),
                                t.getCompletedTime(),
                                t.getFailureReason()))
                .toList();
    }
}
