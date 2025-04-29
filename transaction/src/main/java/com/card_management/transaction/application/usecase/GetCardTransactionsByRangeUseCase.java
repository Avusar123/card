package com.card_management.transaction.application.usecase;

import com.card_management.shared.UseCase;
import com.card_management.shared.dto.TransactionDto;
import com.card_management.shared.dto.UserRole;
import com.card_management.transaction.application.usecase.command.GetCardTransactionsByRangeCommand;
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
public class GetCardTransactionsByRangeUseCase {
    private final TransactionRepo repo;

    @Autowired
    public GetCardTransactionsByRangeUseCase(TransactionRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> execute(@Valid GetCardTransactionsByRangeCommand command) {
        PageRequest pageRequest = PageRequest.of(command.page(), command.pageSize());

        var transactions = switch (command.type()) {
            case INCOME -> repo.findIncomeInPeriod(
                    command.cardId(),
                    LocalDateTime.now().minusDays(command.range().getDays()),
                    pageRequest);
            case OUTCOME -> repo.findOutcomeInPeriod(
                    command.cardId(),
                    LocalDateTime.now().minusDays(command.range().getDays()),
                    pageRequest);
        };

        if (!transactions.isEmpty()) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser user
                    && !(user.getRole() == UserRole.ADMIN || user.getId().equals(transactions.getContent().getFirst().getInitiator()))) {
                throw new AccessDeniedException("You do not have access to see history!");
            }
        }

        return transactions.getContent().stream()
                .map(t -> new TransactionDto(
                        t.getId(),
                        t.getInitiator(),
                        t.getFromId(),
                        t.getToId(),
                        t.getAmount(),
                        t.getStatus(),
                        t.getFailureReason()))
                .toList();
    }
}
