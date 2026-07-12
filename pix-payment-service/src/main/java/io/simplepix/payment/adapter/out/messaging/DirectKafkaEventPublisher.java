package io.simplepix.payment.adapter.out.messaging;

import io.simplepix.payment.application.port.out.DomainEventPublisher;
import io.simplepix.payment.domain.event.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Qualifier("directKafka")
public class DirectKafkaEventPublisher implements DomainEventPublisher {

    private static final String TOPIC = "pix.payment.events";

    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    public DirectKafkaEventPublisher(KafkaTemplate<String, DomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        kafkaTemplate.send(TOPIC, event.aggregateId().toString(), event);
    }
}