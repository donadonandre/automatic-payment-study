package io.simplepix.payment.application.port.in;

import io.simplepix.payment.domain.model.Money;

import java.util.UUID;

public interface InitiatePaymentUseCase {

    PaymentResult initiate(InitiatePaymentCommand command);

    record InitiatePaymentCommand(
            UUID sourceAccountId,
            String targetPixKeyValue,
            Money amount,
            String idempotencyKey
    ) {}

    record PaymentResult(
            UUID paymentId,
            String status
    ) {}
}