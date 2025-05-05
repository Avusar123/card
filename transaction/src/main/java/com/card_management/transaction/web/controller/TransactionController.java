package com.card_management.transaction.web.controller;

import com.card_management.shared.TimeRange;
import com.card_management.shared.dto.TransactionDto;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import com.card_management.shared.id.UserId;
import com.card_management.transaction.application.usecase.CreateTransactionUseCase;
import com.card_management.transaction.application.usecase.GetCardTransactionsByRangeUseCase;
import com.card_management.transaction.application.usecase.GetTransactionByIdUseCase;
import com.card_management.transaction.application.usecase.GetUserTransactionsByRangeUseCase;
import com.card_management.transaction.application.usecase.command.GetCardTransactionsByRangeCommand;
import com.card_management.transaction.application.usecase.command.GetTransactionByIdCommand;
import com.card_management.transaction.application.usecase.command.GetUserTransactionsByRangeCommand;
import com.card_management.transaction.application.usecase.command.OperationType;
import com.card_management.transaction.application.usecase.request.CreateTransactionRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetCardTransactionsByRangeUseCase getCardTransactionsByRangeUseCase;
    private final GetTransactionByIdUseCase getTransactionByIdUseCase;
    private final GetUserTransactionsByRangeUseCase getUserTransactionsByRangeUseCase;

    @Autowired
    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
                                 GetCardTransactionsByRangeUseCase getCardTransactionsByRangeUseCase,
                                 GetTransactionByIdUseCase getTransactionByIdUseCase,
                                 GetUserTransactionsByRangeUseCase getUserTransactionsByRangeUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getCardTransactionsByRangeUseCase = getCardTransactionsByRangeUseCase;
        this.getTransactionByIdUseCase = getTransactionByIdUseCase;
        this.getUserTransactionsByRangeUseCase = getUserTransactionsByRangeUseCase;
    }

    @PostMapping("/transaction/")
    public TransactionDto create(@RequestBody @Valid CreateTransactionRequest request) {
        return createTransactionUseCase.execute(request.toCommand());
    }

    @GetMapping("/transaction/history")
    public List<TransactionDto> getTransactionHistory(@RequestParam(value = "cardId") UUID cardId,
                                                      @RequestParam(value = "range") TimeRange range,
                                                      @RequestParam(value = "type") OperationType type,
                                                      @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") @Positive int pageSize) {
        return getCardTransactionsByRangeUseCase.execute(new GetCardTransactionsByRangeCommand(
                new CardId(cardId),
                range,
                type,
                page,
                pageSize
        ));
    }

    @GetMapping("/transaction/{transactionId}")
    public TransactionDto getTransactionById(@PathVariable UUID transactionId) {
        return getTransactionByIdUseCase.execute(new GetTransactionByIdCommand(new TransactionId(transactionId)));
    }

    @GetMapping("/transaction/user-history")
    public List<TransactionDto> getUserTransactionHistory(@RequestParam(value = "userId") UUID userId,
                                                          @RequestParam(value = "range") TimeRange range,
                                                          @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") @Positive int pageSize) {
        return getUserTransactionsByRangeUseCase.execute(new GetUserTransactionsByRangeCommand(
                new UserId(userId),
                range,
                page,
                pageSize
        ));
    }
}