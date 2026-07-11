package io.simplepix.payment.domain.event;

import java.time.Instant;
import java.util.UUID;

public sealed interface DomainEvent permits PaymentInitiated, PaymentCompleted, PaymentFailed {
    UUID eventId();
    UUID aggregateId();
    Instant occurredAt();
}