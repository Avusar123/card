package com.card_management.limit.application.usecase;

import com.card_management.limit.application.usecase.command.GetLimitByIdCommand;
import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.dto.LimitDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetLimitByIdUseCase {

    private final LimitRepo repo;

    @Autowired
    public GetLimitByIdUseCase(LimitRepo repo) {
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
