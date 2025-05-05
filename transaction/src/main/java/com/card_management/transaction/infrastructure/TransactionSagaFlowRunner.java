package com.card_management.transaction.infrastructure;

import com.card_management.shared.dto.TransactionMiniDto;
import com.card_management.shared.dto.TransactionStatus;
import com.card_management.transaction.infrastructure.saga.producer.TransactionSagaEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionSagaFlowRunner implements ApplicationRunner {

    private final TransactionRepo repo;
    private final TransactionSagaEntryPoint entryPoint;

    @Autowired
    public TransactionSagaFlowRunner(TransactionRepo repo,
                                     TransactionSagaEntryPoint entryPoint) {
        this.repo = repo;
        this.entryPoint = entryPoint;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    protected void periodicRun() {
        var transactions = repo.findAllByStatus(TransactionStatus.PROCESSING);

        for (var transaction : transactions) {
            if (entryPoint.isAlreadyInProgress(transaction.getId())) {
                entryPoint.handleExisting(transaction.getId());
            } else {

                var dto = new TransactionMiniDto(
                        transaction.getId(),
                        transaction.getInitiator(),
                        transaction.getFromId(),
                        transaction.getToId(),
                        transaction.getAmount(),
                        transaction.getCreatedTime()
                );

                if (entryPoint.isEmpty(dto))
                    entryPoint.handleNew(dto);
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void run(ApplicationArguments args) throws Exception {
        periodicRun();
    }
}
