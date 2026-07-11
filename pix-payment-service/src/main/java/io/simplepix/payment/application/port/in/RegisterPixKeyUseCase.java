package io.simplepix.payment.application.port.in;

import io.simplepix.payment.domain.model.PixKeyType;

import java.util.UUID;

public interface RegisterPixKeyUseCase {

    void register(RegisterPixKeyCommand command);

    record RegisterPixKeyCommand(
            UUID accountId,
            String keyValue,
            PixKeyType keyType
    ) {}
}