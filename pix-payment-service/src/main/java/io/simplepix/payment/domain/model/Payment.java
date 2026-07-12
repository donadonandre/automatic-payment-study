package io.simplepix.payment.domain.model;

import java.time.Instant;
import java.util.UUID;

public final class Payment {

    private final UUID id;
    private final UUID sourceAccountId;
    private final PixKey targetPixKey;
    private final Money amount;
    private PaymentStatus status;
    private String failureReason;
    private final String idempotencyKey;
    private final Instant createdAt;
    private Instant updatedAt;

    private Payment(UUID id, UUID sourceAccountId, PixKey targetPixKey, Money amount,
                    PaymentStatus status, String failureReason, String idempotencyKey,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetPixKey = targetPixKey;
        this.amount = amount;
        this.status = status;
        this.failureReason = failureReason;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Payment initiate(UUID sourceAccountId, PixKey targetPixKey, Money amount, String idempotencyKey) {
        if (sourceAccountId == null) {
            throw new IllegalArgumentException("Source account id is required");
        }
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }
        Instant now = Instant.now();
        return new Payment(UUID.randomUUID(), sourceAccountId, targetPixKey, amount,
                PaymentStatus.PENDING, null, idempotencyKey, now, now);
    }

    public static Payment reconstitute(UUID id, UUID sourceAccountId, PixKey targetPixKey, Money amount,
                                       PaymentStatus status, String failureReason, String idempotencyKey,
                                       Instant createdAt, Instant updatedAt) {
        return new Payment(id, sourceAccountId, targetPixKey, amount, status, failureReason,
                idempotencyKey, createdAt, updatedAt);
    }

    public void complete() {
        transitionTo(PaymentStatus.COMPLETED);
        this.failureReason = null;
    }

    public void fail(String reason) {
        transitionTo(PaymentStatus.FAILED);
        this.failureReason = reason;
    }

    public void reverse() {
        transitionTo(PaymentStatus.REVERSED);
    }

    private void transitionTo(PaymentStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new IllegalStateException(
                    "Cannot transition payment %s from %s to %s".formatted(id, status, target));
        }
        this.status = target;
        this.updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public UUID sourceAccountId() { return sourceAccountId; }
    public PixKey targetPixKey() { return targetPixKey; }
    public Money amount() { return amount; }
    public PaymentStatus status() { return status; }
    public String failureReason() { return failureReason; }
    public String idempotencyKey() { return idempotencyKey; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}