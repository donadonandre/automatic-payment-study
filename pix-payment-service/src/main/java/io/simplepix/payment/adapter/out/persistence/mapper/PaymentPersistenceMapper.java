package io.simplepix.payment.adapter.out.persistence.mapper;

import io.simplepix.payment.adapter.out.persistence.entity.PaymentEntity;
import io.simplepix.payment.domain.model.Money;
import io.simplepix.payment.domain.model.Payment;
import io.simplepix.payment.domain.model.PixKey;
import io.simplepix.payment.domain.model.PixKeyType;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(
                payment.id(),
                payment.sourceAccountId(),
                payment.targetPixKey().value(),
                payment.amount().cents(),
                payment.amount().currency(),
                payment.status(),
                payment.failureReason(),
                payment.idempotencyKey(),
                0L, // idem nota do AccountPersistenceMapper
                payment.createdAt(),
                payment.updatedAt()
        );
    }

    public Payment toDomain(PaymentEntity entity, PixKeyType targetKeyType) {
        return Payment.reconstitute(
                entity.getId(),
                entity.getSourceAccountId(),
                new PixKey(entity.getTargetPixKey(), targetKeyType),
                new Money(entity.getAmountCents(), entity.getCurrency()),
                entity.getStatus(),
                entity.getFailureReason(),
                entity.getIdempotencyKey(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}