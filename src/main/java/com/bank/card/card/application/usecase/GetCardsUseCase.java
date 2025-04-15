package com.bank.card.card.application.usecase;

import com.bank.card.card.application.usecase.command.GetCardsCommand;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.dto.CardDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
public class GetCardsUseCase {

    private final CardRepo repo;

    @Autowired
    public GetCardsUseCase(CardRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CardDto> execute(@Valid GetCardsCommand command) {
        var cards = repo.findAll(PageRequest.of(command.page(), command.size()));

        return cards.map(card -> new CardDto(
                card.getId(),
                card.getOwner().getName(),
                card.getOwner().getId(),
                card.getNumber().mask(),
                card.getStatus(),
                card.getCardValue(),
                card.getExpires()
        )).toList();
    }
}
