package com.bank.card.transaction.application.usecase;

import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.LimitDto;
import com.bank.card.shared.dto.SecurityUser;
import com.bank.card.shared.dto.TransactionDto;
import com.bank.card.shared.exception.NotEnoughMoney;
import com.bank.card.shared.id.TransactionId;
import com.bank.card.transaction.application.CardBalanceManager;
import com.bank.card.transaction.application.CardProvider;
import com.bank.card.transaction.application.LimitProvider;
import com.bank.card.transaction.application.exception.LimitExceeded;
import com.bank.card.transaction.application.usecase.command.CreateTransactionCommand;
import com.bank.card.transaction.domain.Transaction;
import com.bank.card.transaction.infrastructure.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;

@UseCase
public class CreateTransactionUseCase {

    private final TransactionRepo repo;
    private final LimitProvider limitProvider;
    private final CardProvider provider;
    private final CardBalanceManager balanceManager;

    @Autowired
    public CreateTransactionUseCase(TransactionRepo repo,
                                    LimitProvider limitProvider,
                                    CardProvider provider,
                                    CardBalanceManager balanceManager) {
        this.repo = repo;
        this.limitProvider = limitProvider;
        this.provider = provider;
        this.balanceManager = balanceManager;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionDto execute(CreateTransactionCommand command) {
        var ownerId = provider.getOwnerByRaw(command.fromNumber());

        var fromId = provider.getIdFromRaw(command.fromNumber());

        var toId = provider.getIdFromRaw(command.toNumber());

        if (fromId == toId) {
            throw new IllegalArgumentException("You must transfer to another card!");
        }

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser user &&
                !ownerId.equals(user.getId())) {
            throw new AccessDeniedException("This card is not belongs to that user!");
        }

        if (!balanceManager.canSubtract(command.amount(), fromId)) {
            throw new NotEnoughMoney(command.fromNumber(), command.amount());
        }

        var limits = limitProvider.getAll(fromId);

        var maxRangeLimit = limits.stream().max(Comparator.comparing(limit ->
                        limit
                                .id()
                                .range()
                                .getDays()))
                .orElseGet(() -> LimitDto.maxLimit(fromId));


        var transactionsPage = repo.findOutcomeInPeriod(
                fromId,
                LocalDateTime.now().minusDays(
                        maxRangeLimit
                                .id()
                                .range()
                                .getDays()),
                PageRequest.of(0, Integer.MAX_VALUE)
        );

        var transactions = transactionsPage.getContent();

        for (var limit : limits) {
            var time = LocalDateTime.now().minusDays(limit.id().range().getDays());

            var sum = transactions
                    .stream()
                    .filter(t -> t.getTime().isAfter(time))
                    .mapToInt(Transaction::getAmount)
                    .sum();

            if (sum + command.amount() > limit.amount()) {
                throw new LimitExceeded("You hit limits on source card!");
            }
        }

        balanceManager.subtract(command.amount(), fromId);

        balanceManager.add(command.amount(), toId);

        var transaction = new Transaction(
                new TransactionId(),
                fromId,
                toId,
                ownerId,
                LocalDateTime.now(),
                command.amount()
        );

        repo.save(transaction);

        return new TransactionDto(
                transaction.getId(),
                transaction.getInitiator(),
                transaction.getFromId(),
                transaction.getToId(),
                transaction.getAmount()
        );
    }
}
