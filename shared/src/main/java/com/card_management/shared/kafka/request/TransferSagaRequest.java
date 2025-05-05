package com.card_management.shared.kafka.request;

import com.card_management.shared.dto.TransactionDto;
import com.card_management.shared.dto.TransactionMiniDto;

public record TransferSagaRequest(TransactionMiniDto dto) {
}
