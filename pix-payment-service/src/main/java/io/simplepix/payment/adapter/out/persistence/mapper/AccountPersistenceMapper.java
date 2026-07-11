package io.simplepix.payment.adapter.out.persistence.mapper;

import io.simplepix.payment.adapter.out.persistence.entity.AccountEntity;
import io.simplepix.payment.domain.model.Account;
import io.simplepix.payment.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {

    public AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.id(),
                account.holderName(),
                account.documentNumber(),
                account.balance().cents(),
                account.balance().currency(),
                0L, // version é controlado pelo Hibernate a partir da linha existente; ver nota no adapter
                account.createdAt(),
                account.updatedAt()
        );
    }

    public Account toDomain(AccountEntity entity) {
        return Account.reconstitute(
                entity.getId(),
                entity.getHolderName(),
                entity.getDocumentNumber(),
                new Money(entity.getBalanceCents(), entity.getCurrency()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}