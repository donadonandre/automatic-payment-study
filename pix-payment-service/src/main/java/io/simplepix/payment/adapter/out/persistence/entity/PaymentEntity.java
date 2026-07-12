package io.simplepix.payment.adapter.out.persistence.entity;

import io.simplepix.payment.domain.model.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private UUID id;

    @Column(name = "source_account_id", nullable = false)
    private UUID sourceAccountId;

    @Column(name = "target_pix_key", nullable = false)
    private String targetPixKey;

    @Column(name = "amount_cents", nullable = false)
    private long amountCents;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "payment_status")
    private PaymentStatus status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected PaymentEntity() {
        // JPA
    }

    public PaymentEntity(UUID id, UUID sourceAccountId, String targetPixKey, long amountCents, String currency,
                         PaymentStatus status, String failureReason, String idempotencyKey, long version,
                         Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetPixKey = targetPixKey;
        this.amountCents = amountCents;
        this.currency = currency;
        this.status = status;
        this.failureReason = failureReason;
        this.idempotencyKey = idempotencyKey;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public String getTargetPixKey() { return targetPixKey; }
    public long getAmountCents() { return amountCents; }
    public String getCurrency() { return currency; }
    public PaymentStatus getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    /**
     * Applies a new status and failure reason to this managed entity, letting Hibernate's dirty
     * checking handle the UPDATE on commit. Package-adjacent visibility isn't expressible in Java
     * without JPMS, so this is public by necessity — intended for use by
     * {@code PaymentPersistenceAdapter} only.
     */
    public void applyStatus(PaymentStatus status, String failureReason, Instant updatedAt) {
        this.status = status;
        this.failureReason = failureReason;
        this.updatedAt = updatedAt;
    }
}