package com.bank.card.web.controller;

import com.bank.card.shared.TimeRange;
import com.bank.card.shared.dto.TransactionDto;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.TransactionId;
import com.bank.card.shared.id.UserId;
import com.bank.card.transaction.application.usecase.CreateTransactionUseCase;
import com.bank.card.transaction.application.usecase.GetCardTransactionsByRangeUseCase;
import com.bank.card.transaction.application.usecase.GetTransactionByIdUseCase;
import com.bank.card.transaction.application.usecase.GetUserTransactionsByRangeUseCase;
import com.bank.card.transaction.application.usecase.command.CreateTransactionCommand;
import com.bank.card.transaction.application.usecase.command.GetCardTransactionsByRangeCommand;
import com.bank.card.transaction.application.usecase.command.GetTransactionByIdCommand;
import com.bank.card.transaction.application.usecase.command.GetUserTransactionsByRangeCommand;
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
    public TransactionDto create(@RequestBody @Valid CreateTransactionCommand command) {
        return createTransactionUseCase.execute(command);
    }

    @GetMapping("/transaction/history")
    public List<TransactionDto> getTransactionHistory(@RequestParam(value = "cardId") UUID cardId,
                                                      @RequestParam(value = "range") TimeRange range,
                                                      @RequestParam(value = "income", defaultValue = "false") boolean income,
                                                      @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") @Positive int pageSize) {
        return getCardTransactionsByRangeUseCase.execute(new GetCardTransactionsByRangeCommand(
                new CardId(cardId),
                range,
                income,
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