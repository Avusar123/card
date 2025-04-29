package com.card_management.card.application.usecase;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.application.usecase.command.GetCardByNumberCommand;
import com.card_management.card.application.usecase.command.GetCardsByUserIdCommand;
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

import java.util.List;

@UseCase
public class GetCardByNumberUseCase {


    private final CardRepo repo;
    private final CardNumberEncoder cardNumberEncoder;

    @Autowired
    public GetCardByNumberUseCase(CardRepo repo,
                                   CardNumberEncoder cardNumberEncoder) {
        this.repo = repo;
        this.cardNumberEncoder = cardNumberEncoder;
    }

    @Transactional(readOnly = true)
    public CardDto execute(@Valid GetCardByNumberCommand command) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                instanceof SecurityUser user) {

            var card = repo.findByHashedNumber(cardNumberEncoder.encode(command.rawNumber()).hashed())
                    .orElseThrow(() -> new EntityNotFoundException("Card not found!"));

            if (card.getOwnerId().equals(user.getId())) {
                return new CardDto(
                        card.getId(),
                        card.getOwnerId(),
                        command.rawNumber(),
                        card.getStatus(),
                        card.getCardValue(),
                        card.getExpires()
                );
            } else {
                return new CardDto(
                        card.getId(),
                        card.getOwnerId(),
                        card.getNumber().mask(),
                        null,
                        null,
                        null
                );
            }
        }

        throw new AccessDeniedException("You should re-login!");
    }
}
