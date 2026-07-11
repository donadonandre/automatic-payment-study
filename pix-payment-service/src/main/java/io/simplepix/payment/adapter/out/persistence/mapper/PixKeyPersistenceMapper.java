package io.simplepix.payment.adapter.out.persistence.mapper;

import io.simplepix.payment.adapter.out.persistence.entity.PixKeyEntity;
import io.simplepix.payment.domain.model.PixKey;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PixKeyPersistenceMapper {

    public PixKeyEntity toEntity(UUID accountId, PixKey pixKey) {
        return new PixKeyEntity(accountId, pixKey.value(), pixKey.type());
    }

    public PixKey toDomain(PixKeyEntity entity) {
        return new PixKey(entity.getKeyValue(), entity.getKeyType());
    }
}
