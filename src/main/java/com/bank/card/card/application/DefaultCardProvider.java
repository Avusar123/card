package com.bank.card.card.application;

import com.bank.card.card.domain.Card;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.api.CardNumberEncoder;
import com.bank.card.shared.dto.CardDto;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import com.bank.card.transaction.application.CardProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCardProvider implements CardProvider {

    private final CardRepo repo;
    private final CardNumberEncoder encoder;

    @Autowired
    public DefaultCardProvider(CardRepo repo,
                               CardNumberEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public CardDto getById(CardId id) {
        var card = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not exist!"));

        return generateDto(card);
    }

    @Override
    public CardDto getByHash(String hash) {
        var card = repo.findByHashedNumber(hash).orElseThrow(() -> new EntityNotFoundException("Card not exist!"));

        return generateDto(card);
    }

    @Override
    public UserId getOwnerByRaw(String rawNumber) {
        return repo.findOwnerByHashed(encoder.encode(rawNumber).hashed()).orElseThrow(
                () -> new EntityNotFoundException("Card owner is not found!")
        );
    }

    @Override
    public CardId getIdFromRaw(String rawNumber) {
        return repo.findIdByHashed(encoder.encode(rawNumber).hashed()).orElseThrow(
                () -> new EntityNotFoundException("Card id is not found!")
        );
    }

    @Override
    public boolean exist(CardId id) {
        return repo.existsById(id);
    }

    private CardDto generateDto(Card card) {
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
