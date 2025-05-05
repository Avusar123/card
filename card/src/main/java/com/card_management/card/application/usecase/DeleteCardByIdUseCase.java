package com.card_management.card.application.usecase;

import com.card_management.card.application.CardDeletedProducer;
import com.card_management.card.application.usecase.command.DeleteCardByIdCommand;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.kafka.event.CardListDeletedEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
public class DeleteCardByIdUseCase {

    private final CardDeletedProducer producer;
    CardRepo repo;

    @Autowired
    public DeleteCardByIdUseCase(CardRepo repo,
                                 CardDeletedProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void execute(@Valid DeleteCardByIdCommand command) {
        var id = command.id();

        repo.deleteById(id);

        producer.send(new CardListDeletedEvent(List.of(id)));
    }
}
