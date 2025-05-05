package com.card_management.shared.kafka.request;

import com.card_management.shared.TimeRange;
import com.card_management.shared.dto.TransactionDto;
import com.card_management.shared.dto.TransactionMiniDto;

import java.util.Map;

public record LimitsCheckSagaRequest(TransactionMiniDto dto, Map<TimeRange, Integer> sums) {
}
