package io.simplepix.payment.adapter.out.messaging;

import io.simplepix.payment.adapter.out.persistence.entity.OutboxEventEntity;
import io.simplepix.payment.adapter.out.persistence.repository.OutboxEventJpaRepository;
import io.simplepix.payment.application.port.out.DomainEventPublisher;
import io.simplepix.payment.domain.event.DomainEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Primary
@Component
public class OutboxEventPublisher implements DomainEventPublisher {

    private final OutboxEventJpaRepository outboxRepository;
    private final tools.jackson.databind.ObjectMapper objectMapper;

    public OutboxEventPublisher(OutboxEventJpaRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEventEntity entity = new OutboxEventEntity(
                    "Payment",
                    event.aggregateId(),
                    event.getClass().getSimpleName(),
                    payload,
                    event.aggregateId().toString()
            );
            outboxRepository.save(entity);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize domain event: " + event, ex);
        }
    }
}