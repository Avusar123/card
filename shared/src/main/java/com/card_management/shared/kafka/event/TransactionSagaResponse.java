package com.card_management.shared.kafka.event;

import com.card_management.shared.id.TransactionId;

import java.util.Optional;

public record TransactionSagaResponse(TransactionId id,
                                      Optional<String> failureReason,
                                      String serviceName) {
}
