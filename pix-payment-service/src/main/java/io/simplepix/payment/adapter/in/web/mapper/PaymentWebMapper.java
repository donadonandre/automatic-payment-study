package io.simplepix.payment.adapter.in.web.mapper;

import io.simplepix.payment.adapter.in.web.dto.InitiatePaymentRequest;
import io.simplepix.payment.adapter.in.web.dto.InitiatePaymentResponse;
import io.simplepix.payment.application.port.in.InitiatePaymentUseCase.InitiatePaymentCommand;
import io.simplepix.payment.application.port.in.InitiatePaymentUseCase.PaymentResult;
import io.simplepix.payment.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebMapper {

    public InitiatePaymentCommand toCommand(InitiatePaymentRequest request, String idempotencyKey) {
        return new InitiatePaymentCommand(
                request.sourceAccountId(),
                request.targetPixKey(),
                Money.ofReais(request.amount()),
                idempotencyKey
        );
    }

    public InitiatePaymentResponse toResponse(PaymentResult result) {
        return new InitiatePaymentResponse(result.paymentId(), result.status());
    }
}