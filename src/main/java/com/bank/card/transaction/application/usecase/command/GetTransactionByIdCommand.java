package com.bank.card.transaction.application.usecase.command;

import com.bank.card.shared.id.TransactionId;

public record GetTransactionByIdCommand(TransactionId id) {
}
