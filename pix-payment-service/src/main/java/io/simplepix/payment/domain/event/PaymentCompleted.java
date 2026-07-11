package io.simplepix.payment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentCompleted(
        UUID eventId,
        UUID aggregateId,
        Instant occurredAt
) implements DomainEvent {

    public static PaymentCompleted of(UUID paymentId) {
        return new PaymentCompleted(UUID.randomUUID(), paymentId, Instant.now());
    }
}