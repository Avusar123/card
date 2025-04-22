package com.bank.card.limit.application.usecase;

import com.bank.card.limit.application.usecase.command.SetLimitCommand;
import com.bank.card.limit.domain.Limit;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.LimitDto;
import com.bank.card.transaction.application.CardProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class SetLimitUseCase {

    private final LimitRepo repo;
    private final CardProvider cardProvider;

    @Autowired
    public SetLimitUseCase(
            LimitRepo repo,
            CardProvider cardProvider) {
        this.repo = repo;
        this.cardProvider = cardProvider;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public LimitDto execute(@Valid SetLimitCommand command) {
        if (!cardProvider.exist(command.limitId().cardId())) {
            throw new EntityNotFoundException("Card not found!");
        }

        var limitOpt = repo.findById(command.limitId());

        Limit limit;

        limit = limitOpt.orElseGet(() -> new Limit(
                command.limitId(),
                command.amount()
        ));

        limit.setMaxAmount(command.amount());

        repo.save(limit);

        return new LimitDto(
                limit.getId(),
                limit.getMaxAmount()
        );
    }
}
