package io.simplepix.payment.application.port.out;

import io.simplepix.payment.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}