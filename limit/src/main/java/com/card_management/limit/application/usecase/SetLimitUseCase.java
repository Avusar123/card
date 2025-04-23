package com.card_management.limit.application.usecase;

import com.card_management.limit.application.usecase.command.SetLimitCommand;
import com.card_management.limit.domain.Limit;
import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.dto.LimitDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class SetLimitUseCase {

    private final LimitRepo repo;

    @Autowired
    public SetLimitUseCase(
            LimitRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public LimitDto execute(@Valid SetLimitCommand command) {
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
