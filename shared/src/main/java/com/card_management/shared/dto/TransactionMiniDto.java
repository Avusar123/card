package com.card_management.shared.dto;

import com.card_management.shared.dto.TransactionStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import com.card_management.shared.id.UserId;

import java.time.LocalDateTime;

public record TransactionMiniDto(TransactionId id, UserId initiator, CardId from, CardId to, int amount, LocalDateTime addedTime) {
}
