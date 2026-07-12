package io.simplepix.payment.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    private UUID id;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "balance_cents", nullable = false)
    private long balanceCents;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AccountEntity() {
        // JPA
    }

    public AccountEntity(UUID id, String holderName, String documentNumber, long balanceCents,
                         String currency, long version, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.holderName = holderName;
        this.documentNumber = documentNumber;
        this.balanceCents = balanceCents;
        this.currency = currency;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getHolderName() { return holderName; }
    public String getDocumentNumber() { return documentNumber; }
    public long getBalanceCents() { return balanceCents; }
    public String getCurrency() { return currency; }
    public long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    /**
     * Applies a new balance to this managed entity, letting Hibernate's dirty checking
     * handle the UPDATE on commit. Package-adjacent visibility isn't expressible in Java
     * without JPMS, so this is public by necessity — intended for use by
     * {@code AccountPersistenceAdapter} only.
     */
    public void applyBalance(long balanceCents, Instant updatedAt) {
        this.balanceCents = balanceCents;
        this.updatedAt = updatedAt;
    }
}