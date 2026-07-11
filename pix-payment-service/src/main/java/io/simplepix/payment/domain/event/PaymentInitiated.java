package io.simplepix.payment.domain.event;

import io.simplepix.payment.domain.model.Money;

import java.time.Instant;
import java.util.UUID;

public record PaymentInitiated(
        UUID eventId,
        UUID aggregateId,
        Instant occurredAt,
        UUID sourceAccountId,
        String targetPixKey,
        Money amount
) implements DomainEvent {

    public static PaymentInitiated of(UUID paymentId, UUID sourceAccountId, String targetPixKey, Money amount) {
        return new PaymentInitiated(UUID.randomUUID(), paymentId, Instant.now(), sourceAccountId, targetPixKey, amount);
    }
}