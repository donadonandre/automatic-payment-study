package io.simplepix.payment.adapter.in.web;

import io.simplepix.payment.adapter.in.web.dto.InitiatePaymentRequest;
import io.simplepix.payment.adapter.in.web.dto.InitiatePaymentResponse;
import io.simplepix.payment.adapter.in.web.mapper.PaymentWebMapper;
import io.simplepix.payment.application.port.in.InitiatePaymentUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final InitiatePaymentUseCase initiatePaymentUseCase;
    private final PaymentWebMapper mapper;

    public PaymentController(InitiatePaymentUseCase initiatePaymentUseCase, PaymentWebMapper mapper) {
        this.initiatePaymentUseCase = initiatePaymentUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<InitiatePaymentResponse> initiate(
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
            @Valid @RequestBody InitiatePaymentRequest request) {

        var command = mapper.toCommand(request, idempotencyKey);
        var result = initiatePaymentUseCase.initiate(command);
        var response = mapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}