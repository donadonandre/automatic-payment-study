package io.simplepix.payment.domain.exception;

import io.simplepix.payment.domain.model.Money;

import java.util.UUID;

public final class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(UUID accountId, Money balance, Money requested) {
        super("Account %s has insufficient funds: balance=%d requested=%d"
                .formatted(accountId, balance.cents(), requested.cents()));
    }
}