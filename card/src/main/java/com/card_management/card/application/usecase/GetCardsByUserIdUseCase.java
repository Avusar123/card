package com.card_management.card.application.usecase;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.application.usecase.command.GetCardsByUserIdCommand;
import com.card_management.card.domain.Card;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.dto.CardDto;
import com.card_management.shared.dto.SecurityUser;
import com.card_management.shared.dto.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
public class GetCardsByUserIdUseCase {

    private final CardRepo repo;
    private final CardNumberEncoder cardNumberEncoder;

    @Autowired
    public GetCardsByUserIdUseCase(CardRepo repo,
                                   CardNumberEncoder cardNumberEncoder) {
        this.repo = repo;
        this.cardNumberEncoder = cardNumberEncoder;
    }

    @Transactional(readOnly = true)
    public List<CardDto> execute(@Valid GetCardsByUserIdCommand command) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                instanceof SecurityUser user) {
            if (user.getId().equals(command.userId())) {
                var cards = getCards(command);

                return cards.map(card -> new CardDto(
                        card.getId(),
                        card.getOwnerId(),
                        cardNumberEncoder.decode(card.getNumber().hashed()),
                        card.getStatus(),
                        card.getCardValue(),
                        card.getExpires()
                )).toList();
            } else if (user.getRole() == UserRole.ADMIN) {
                var cards = getCards(command);

                return cards.map(card -> new CardDto(
                        card.getId(),
                        card.getOwnerId(),
                        card.getNumber().mask(),
                        card.getStatus(),
                        card.getCardValue(),
                        card.getExpires()
                )).toList();
            }
        }

        throw new AccessDeniedException("You do not have access to card list");
    }

    private Page<Card> getCards(GetCardsByUserIdCommand command) {
        return repo.findAllByOwnerId(command.userId(), PageRequest.of(command.page(), command.size()));
    }
}
