package com.bank.card.card.application.usecase;

import com.bank.card.card.application.usecase.command.GetCardByIdCommand;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.CardNumberEncoder;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.CardDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetCardByIdUseCase {

    private final CardRepo repo;
    private final CardNumberEncoder cardNumberEncoder;

    @Autowired
    public GetCardByIdUseCase(CardRepo repo,
                              CardNumberEncoder cardNumberEncoder) {
        this.repo = repo;
        this.cardNumberEncoder = cardNumberEncoder;
    }

    @Transactional(readOnly = true)
    public CardDto execute(@Valid GetCardByIdCommand command) {
        var card = repo.findById(command.id()).orElseThrow(() -> new EntityNotFoundException("Card not exist!"));

        if (card.getOwner().getEmail().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName())) {
            return new CardDto(
                    card.getId(),
                    card.getOwner().getName(),
                    card.getOwner().getId(),
                    cardNumberEncoder.decode(card.getNumber().hashed()),
                    card.getStatus(),
                    card.getCardValue(),
                    card.getExpires()
            );
        }

        return new CardDto(
                card.getId(),
                card.getOwner().getName(),
                card.getOwner().getId(),
                card.getNumber().mask(),
                card.getStatus(),
                card.getCardValue(),
                card.getExpires()
        );
    }
}
