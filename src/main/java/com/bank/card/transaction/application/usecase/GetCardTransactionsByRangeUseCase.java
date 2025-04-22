package com.bank.card.transaction.application.usecase;

import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.SecurityUser;
import com.bank.card.shared.dto.TransactionDto;
import com.bank.card.transaction.application.usecase.command.GetCardTransactionsByRangeCommand;
import com.bank.card.transaction.domain.Transaction;
import com.bank.card.transaction.infrastructure.TransactionRepo;
import com.bank.card.user.domain.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        Page<Transaction> transactionsPage;

        PageRequest pageRequest = PageRequest.of(command.page(), command.pageSize());

        if (command.income()) {
            transactionsPage = repo.findIncomeInPeriod(
                    command.cardId(),
                    LocalDateTime.now().minusDays(command.range().getDays()),
                    pageRequest
            );
        } else {
            transactionsPage = repo.findOutcomeInPeriod(
                    command.cardId(),
                    LocalDateTime.now().minusDays(command.range().getDays()),
                    pageRequest
            );
        }

        if (!transactionsPage.isEmpty()) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser user
                    && !(user.getRole() == UserRole.ADMIN || user.getId().equals(transactionsPage.getContent().getFirst().getInitiator()))) {
                throw new AccessDeniedException("You do not have access to see history!");
            }
        }

        return transactionsPage.getContent().stream()
                .map(t -> new TransactionDto(
                        t.getId(),
                        t.getInitiator(),
                        t.getFromId(),
                        t.getToId(),
                        t.getAmount()))
                .toList();
    }
}
