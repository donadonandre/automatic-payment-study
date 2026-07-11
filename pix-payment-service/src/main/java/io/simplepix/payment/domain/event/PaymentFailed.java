package io.simplepix.payment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailed(
        UUID eventId,
        UUID aggregateId,
        Instant occurredAt,
        String reason
) implements DomainEvent {

    public static PaymentFailed of(UUID paymentId, String reason) {
        return new PaymentFailed(UUID.randomUUID(), paymentId, Instant.now(), reason);
    }
}