package com.bank.card.web.controller;

import com.bank.card.limit.application.usecase.*;
import com.bank.card.limit.application.usecase.request.*;
import com.bank.card.limit.domain.LimitRange;
import com.bank.card.shared.dto.LimitDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class LimitController {
    private final SetLimitUseCase setLimitUseCase;
    private final GetLimitByIdUseCase getLimitByIdUseCase;
    private final GetLimitsByCardIdUseCase getLimitsByCardIdUseCase;
    private final DeleteLimitByIdUseCase deleteLimitByIdUseCase;
    private final DeleteLimitsByCardIdUseCase deleteLimitsByCardIdUseCase;

    @Autowired
    public LimitController(SetLimitUseCase setLimitUseCase,
                           GetLimitByIdUseCase getLimitByIdUseCase,
                           GetLimitsByCardIdUseCase getLimitsByCardIdUseCase,
                           DeleteLimitByIdUseCase deleteLimitByIdUseCase,
                           DeleteLimitsByCardIdUseCase deleteLimitsByCardIdUseCase) {
        this.setLimitUseCase = setLimitUseCase;
        this.getLimitByIdUseCase = getLimitByIdUseCase;
        this.getLimitsByCardIdUseCase = getLimitsByCardIdUseCase;
        this.deleteLimitByIdUseCase = deleteLimitByIdUseCase;
        this.deleteLimitsByCardIdUseCase = deleteLimitsByCardIdUseCase;
    }

    @PostMapping("/limit")
    public LimitDto set(@RequestBody @Valid SetLimitRequest request) {
        return setLimitUseCase.execute(request.toCommand());
    }

    @GetMapping("/limit")
    public LimitDto get(@RequestParam UUID cardId, @RequestParam LimitRange range) {
        return getLimitByIdUseCase.execute(new GetLimitByIdRequest(cardId, range).toCommand());
    }

    @GetMapping("/limit/all")
    public List<LimitDto> getAll(@RequestParam UUID cardId) {
        return getLimitsByCardIdUseCase.execute(new GetLimitsByCardIdRequest(cardId).toCommand());
    }

    @DeleteMapping("/limit")
    public void delete(@RequestBody @Valid DeleteLimitByIdRequest request) {
        deleteLimitByIdUseCase.execute(request.toCommand());
    }

    @DeleteMapping("/limit/all")
    public void delete(@RequestBody @Valid DeleteLimitsByCardIdRequest request) {
        deleteLimitsByCardIdUseCase.execute(request.toCommand());
    }
}
