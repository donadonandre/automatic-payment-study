package io.simplepix.payment.application.port.out;

import io.simplepix.payment.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    Optional<Account> findByIdForUpdate(UUID id);
}