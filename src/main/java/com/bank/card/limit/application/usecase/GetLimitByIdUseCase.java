package com.bank.card.limit.application.usecase;

import com.bank.card.limit.application.usecase.command.GetLimitByIdCommand;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.api.CardNumberEncoder;
import com.bank.card.shared.dto.LimitDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetLimitByIdUseCase {

    private final LimitRepo repo;

    @Autowired
    public GetLimitByIdUseCase(LimitRepo repo,
                               CardNumberEncoder cardNumberEncoder) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public LimitDto execute(@Valid GetLimitByIdCommand command) {
        var limit = repo.findById(command.limitId()).orElseThrow(() -> new EntityNotFoundException("Limit not exist!"));

        return new LimitDto(
                limit.getId(),
                limit.getMaxAmount()
        );
    }
}
