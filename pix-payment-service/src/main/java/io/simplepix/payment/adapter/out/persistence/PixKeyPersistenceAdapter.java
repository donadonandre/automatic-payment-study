package io.simplepix.payment.adapter.out.persistence;

import io.simplepix.payment.adapter.out.persistence.entity.PixKeyEntity;
import io.simplepix.payment.adapter.out.persistence.mapper.PixKeyPersistenceMapper;
import io.simplepix.payment.adapter.out.persistence.repository.PixKeyJpaRepository;
import io.simplepix.payment.application.port.out.PixKeyRepository;
import io.simplepix.payment.domain.model.PixKey;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PixKeyPersistenceAdapter implements PixKeyRepository {

    private final PixKeyJpaRepository jpaRepository;
    private final PixKeyPersistenceMapper mapper;

    public PixKeyPersistenceAdapter(PixKeyJpaRepository jpaRepository, PixKeyPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(UUID accountId, PixKey pixKey) {
        jpaRepository.save(mapper.toEntity(accountId, pixKey));
    }

    @Override
    public Optional<PixKey> findByValue(String keyValue) {
        return jpaRepository.findByKeyValue(keyValue).map(mapper::toDomain);
    }

    @Override
    public Optional<UUID> findAccountIdByKeyValue(String keyValue) {
        return jpaRepository.findByKeyValue(keyValue).map(PixKeyEntity::getAccountId);
    }
}