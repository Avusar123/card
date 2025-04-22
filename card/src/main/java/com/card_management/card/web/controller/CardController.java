package com.card_management.card.web.controller;

import com.card_management.card.application.usecase.*;
import com.card_management.card.application.usecase.command.*;
import com.card_management.shared.dto.CardDto;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class CardController {

    private final CreateCardUseCase createCardUseCase;
    private final GetCardsUseCase getCardsUseCase;
    private final GetCardsByUserIdUseCase getCardsByUserIdUseCase;
    private final BlockCardUseCase blockCardUseCase;
    private final ActivateCardUseCase activateCardUseCase;
    private final GetCardByIdUseCase getCardByIdUseCase;
    private final DeleteCardByIdUseCase deleteCardByIdUseCase;

    @Autowired
    public CardController(CreateCardUseCase createCardUseCase,
                          GetCardsUseCase getCardsUseCase,
                          GetCardsByUserIdUseCase getCardsByUserIdUseCase,
                          BlockCardUseCase blockCardUseCase,
                          ActivateCardUseCase activateCardUseCase,
                          GetCardByIdUseCase getCardByIdUseCase,
                          DeleteCardByIdUseCase deleteCardByIdUseCase) {
        this.createCardUseCase = createCardUseCase;
        this.getCardsUseCase = getCardsUseCase;
        this.getCardsByUserIdUseCase = getCardsByUserIdUseCase;
        this.blockCardUseCase = blockCardUseCase;
        this.activateCardUseCase = activateCardUseCase;
        this.getCardByIdUseCase = getCardByIdUseCase;
        this.deleteCardByIdUseCase = deleteCardByIdUseCase;
    }

    @PostMapping("/card")
    public CardDto create(@RequestBody @Valid CreateCardCommand command) {
        return createCardUseCase.execute(command);
    }

    @GetMapping("/card")
    public CardDto get(@RequestParam(name = "id") UUID cardId) {
        return getCardByIdUseCase.execute(new GetCardByIdCommand(new CardId(cardId)));
    }

    @DeleteMapping("/card")
    public void delete(@RequestParam(name = "id") UUID cardId) {
        deleteCardByIdUseCase.execute(new DeleteCardByIdCommand(new CardId(cardId)));
    }

    @PostMapping("/card/block")
    public void block(@RequestParam(name = "id") UUID cardId) {
        blockCardUseCase.execute(new BlockCardCommand(new CardId(cardId)));
    }

    @PostMapping("/card/activate")
    public void activate(@RequestParam(name = "id") UUID cardId) {
        activateCardUseCase.execute(new ActivateCardCommand(new CardId(cardId)));
    }

    @GetMapping("/card/all")
    public List<CardDto> all(@RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size,
                             @RequestParam(name = "user", required = false) UUID userId) {
        if (userId == null)
            return getCardsUseCase.execute(new GetCardsCommand(page, size));
        else
            return getCardsByUserIdUseCase.execute(new GetCardsByUserIdCommand(new UserId(userId), page, size));
    }
}
