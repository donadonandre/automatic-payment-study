package io.simplepix.payment.adapter.out.persistence;

import io.simplepix.payment.adapter.out.persistence.entity.PaymentEntity;
import io.simplepix.payment.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import io.simplepix.payment.adapter.out.persistence.repository.PaymentJpaRepository;
import io.simplepix.payment.adapter.out.persistence.repository.PixKeyJpaRepository;
import io.simplepix.payment.application.port.out.PaymentRepository;
import io.simplepix.payment.domain.exception.PixKeyNotFoundException;
import io.simplepix.payment.domain.model.Payment;
import io.simplepix.payment.domain.model.PixKeyType;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentPersistenceAdapter implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PixKeyJpaRepository pixKeyJpaRepository;
    private final PaymentPersistenceMapper mapper;

    public PaymentPersistenceAdapter(PaymentJpaRepository jpaRepository,
                                     PixKeyJpaRepository pixKeyJpaRepository,
                                     PaymentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.pixKeyJpaRepository = pixKeyJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        Optional<PaymentEntity> existing = jpaRepository.findById(payment.id());

        PaymentEntity persisted = existing
                .map(entity -> {
                    entity.applyStatus(payment.status(), payment.failureReason(), payment.updatedAt());
                    return entity;
                })
                .orElseGet(() -> jpaRepository.save(mapper.toEntity(payment)));

        return toDomain(persisted);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return jpaRepository.findByIdempotencyKey(idempotencyKey).map(this::toDomain);
    }

    private Payment toDomain(PaymentEntity entity) {
        PixKeyType keyType = pixKeyJpaRepository.findByKeyValue(entity.getTargetPixKey())
                .map(pk -> pk.getKeyType())
                .orElseThrow(() -> new PixKeyNotFoundException(entity.getTargetPixKey()));
        return mapper.toDomain(entity, keyType);
    }
}