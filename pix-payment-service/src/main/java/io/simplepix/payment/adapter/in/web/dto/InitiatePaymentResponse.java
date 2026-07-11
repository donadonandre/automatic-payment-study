package io.simplepix.payment.adapter.in.web.dto;

import java.util.UUID;

public record InitiatePaymentResponse(
        UUID paymentId,
        String status
) {}