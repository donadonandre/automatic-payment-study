package io.simplepix.payment.application.port.out;

import io.simplepix.payment.domain.model.PixKey;

import java.util.Optional;
import java.util.UUID;

public interface PixKeyRepository {
    void save(UUID accountId, PixKey pixKey);
    Optional<PixKey> findByValue(String keyValue);
    Optional<UUID> findAccountIdByKeyValue(String keyValue);
}