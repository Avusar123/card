package com.card_management.shared.dto;

import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import com.card_management.shared.id.UserId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public record TransactionDto(@JsonUnwrapped TransactionId id, @JsonUnwrapped(prefix = "user_") UserId initiator,
                             @JsonUnwrapped(prefix = "card_from_") CardId from,
                             @JsonUnwrapped(prefix = "card_to_") CardId to,
                             int amount,
                             TransactionStatus status,
                             LocalDateTime addedTime,
                             @JsonInclude(JsonInclude.Include.NON_ABSENT) Optional<LocalDateTime> completedTime,
                             @JsonInclude(JsonInclude.Include.NON_ABSENT) Optional<String> failureReason) {

}
