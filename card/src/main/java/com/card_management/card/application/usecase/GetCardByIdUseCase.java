package com.card_management.card.application.usecase;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.application.usecase.command.GetCardByIdCommand;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.dto.CardDto;
import com.card_management.shared.dto.UserRole;
import com.card_management.web_security.SecurityUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser user) {
            if (!(user.getId().equals(card.getOwnerId()) || user.getRole() == UserRole.ADMIN)) {
                throw new AccessDeniedException("You do not have access to this card");
            }

            if (user.getId() == card.getOwnerId()) {
                return new CardDto(
                        card.getId(),
                        card.getOwnerId(),
                        cardNumberEncoder.decode(card.getNumber().hashed()),
                        card.getStatus(),
                        card.getCardValue(),
                        card.getExpires()
                );
            } else if (user.getRole() == UserRole.ADMIN) {
                return new CardDto(
                        card.getId(),
                        card.getOwnerId(),
                        card.getNumber().mask(),
                        card.getStatus(),
                        card.getCardValue(),
                        card.getExpires()
                );
            }

        }

        throw new AccessDeniedException("You should login to get access to this card");
    }
}
