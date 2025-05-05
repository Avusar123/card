package com.card_management.transaction.application.usecase.command;

import com.card_management.shared.id.TransactionId;

public record GetTransactionByIdCommand(TransactionId id) {
}
