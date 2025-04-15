package com.bank.card.card.application.usecase;

import com.bank.card.card.application.usecase.command.GetCardsByUserIdCommand;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.CardNumberEncoder;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.UserProvider;
import com.bank.card.shared.dto.CardDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@UseCase
public class GetCardsByUserIdUseCase {

    private final CardRepo repo;
    private final UserProvider userProvider;
    private final CardNumberEncoder cardNumberEncoder;

    @Autowired
    public GetCardsByUserIdUseCase(CardRepo repo,
                                   UserProvider userProvider,
                                   CardNumberEncoder cardNumberEncoder) {
        this.repo = repo;
        this.userProvider = userProvider;
        this.cardNumberEncoder = cardNumberEncoder;
    }

    @Transactional(readOnly = true)
    public List<CardDto> execute(@Valid GetCardsByUserIdCommand command) {
        var cards = repo.findAllByOwnerId(command.userId(), PageRequest.of(command.page(), command.size()));

        if (cards.isEmpty()) {
            return new ArrayList<>();
        } else if (userProvider.getById(command.userId()).email().equals(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName())) {
            return cards.map(card -> new CardDto(
                    card.getId(),
                    card.getOwner().getName(),
                    card.getOwner().getId(),
                    cardNumberEncoder.decode(card.getNumber().hashed()),
                    card.getStatus(),
                    card.getCardValue(),
                    card.getExpires()
            )).toList();
        }

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
