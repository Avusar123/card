package com.bank.card.limit.application.usecase;

import com.bank.card.limit.application.usecase.command.GetLimitsByCardIdCommand;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.LimitDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
public class GetLimitsByCardIdUseCase {

    private final LimitRepo repo;

    @Autowired
    public GetLimitsByCardIdUseCase(LimitRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<LimitDto> execute(@Valid GetLimitsByCardIdCommand command) {
        var limits = repo.findAllByCardId(command.cardId());

        return limits.stream().map(
                limit -> new LimitDto(
                        limit.getId(),
                        limit.getMaxAmount()
                )).toList();
    }
}
