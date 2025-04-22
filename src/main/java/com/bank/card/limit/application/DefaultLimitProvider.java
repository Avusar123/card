package com.bank.card.limit.application;

import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.dto.LimitDto;
import com.bank.card.shared.id.CardId;
import com.bank.card.transaction.application.LimitProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultLimitProvider implements LimitProvider {

    private final LimitRepo repo;

    @Autowired
    public DefaultLimitProvider(LimitRepo repo) {
        this.repo = repo;
    }

    @Override
    public Set<LimitDto> getAll(CardId cardId) {
        return repo.findAllByCardId(cardId)
                .stream()
                .map(l -> new LimitDto(l.getId(), l.getMaxAmount()))
                .collect(Collectors.toSet());
    }
}
