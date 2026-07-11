package io.simplepix.payment.adapter.out.persistence.adapter;

import io.simplepix.payment.adapter.out.persistence.entity.AccountEntity;
import io.simplepix.payment.adapter.out.persistence.mapper.AccountPersistenceMapper;
import io.simplepix.payment.adapter.out.persistence.repository.AccountJpaRepository;
import io.simplepix.payment.application.port.out.AccountRepository;
import io.simplepix.payment.domain.model.Account;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountPersistenceAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;
    private final AccountPersistenceMapper mapper;

    public AccountPersistenceAdapter(AccountJpaRepository jpaRepository, AccountPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Account save(Account account) {
        Optional<AccountEntity> existing = jpaRepository.findById(account.id());

        AccountEntity persisted = existing
                .map(entity -> {
                    entity.applyBalance(account.balance().cents(), account.updatedAt());
                    return entity; // gerenciada: dirty checking cuida do UPDATE
                })
                .orElseGet(() -> jpaRepository.save(mapper.toEntity(account)));

        return mapper.toDomain(persisted);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByIdForUpdate(UUID id) {
        return jpaRepository.findByIdForUpdate(id).map(mapper::toDomain);
    }
}