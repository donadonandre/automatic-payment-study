package io.simplepix.payment.adapter.out.persistence.entity;

import io.simplepix.payment.domain.model.PixKeyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pix_keys")
public class PixKeyEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "key_value", nullable = false, unique = true)
    private String keyValue;

    @Enumerated
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "key_type", nullable = false, columnDefinition = "pix_key_type")
    private PixKeyType keyType;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected PixKeyEntity() {
        // JPA
    }

    public PixKeyEntity(UUID accountId, String keyValue, PixKeyType keyType) {
        this.accountId = accountId;
        this.keyValue = keyValue;
        this.keyType = keyType;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public String getKeyValue() { return keyValue; }
    public PixKeyType getKeyType() { return keyType; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
}