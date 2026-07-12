package io.simplepix.payment.application.port.in;

import io.simplepix.payment.domain.model.Account;

import java.util.UUID;

public interface FindAccountUseCase {
    Account findById(UUID accountId);
}