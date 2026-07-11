package io.simplepix.payment.domain.model;

public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REVERSED;

    public boolean canTransitionTo(PaymentStatus target) {
        return switch (this) {
            case PENDING -> target == COMPLETED || target == FAILED;
            case COMPLETED -> target == REVERSED;
            case FAILED, REVERSED -> false;
        };
    }
}
