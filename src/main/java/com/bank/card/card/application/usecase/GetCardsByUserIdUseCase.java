package com.bank.card.card.application.usecase;

import com.bank.card.card.application.usecase.command.GetCardsByUserIdCommand;
import com.bank.card.card.domain.Card;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.CardNumberEncoder;
import com.bank.card.shared.SecurityUser;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.UserProvider;
import com.bank.card.shared.dto.CardDto;
import com.bank.card.user.domain.UserRole;
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
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                instanceof SecurityUser user) {
            if (user.getId().equals(command.userId())) {
                var cards = getCards(command);

                return cards.map(card -> new CardDto(
                        card.getId(),
                        card.getOwner().getName(),
                        card.getOwner().getId(),
                        cardNumberEncoder.decode(card.getNumber().hashed()),
                        card.getStatus(),
                        card.getCardValue(),
                        card.getExpires()
                )).toList();
            } else if (user.getRole() == UserRole.ADMIN) {
                var cards = getCards(command);

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

        throw new AccessDeniedException("You do not have access to card list");
    }

    private Page<Card> getCards(GetCardsByUserIdCommand command) {
        return repo.findAllByOwnerId(command.userId(), PageRequest.of(command.page(), command.size()));
    }
}
