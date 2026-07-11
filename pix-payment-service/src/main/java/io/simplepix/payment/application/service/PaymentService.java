package io.simplepix.payment.application.service;

import io.simplepix.payment.application.port.in.InitiatePaymentUseCase;
import io.simplepix.payment.application.port.out.AccountRepository;
import io.simplepix.payment.application.port.out.DomainEventPublisher;
import io.simplepix.payment.application.port.out.PaymentRepository;
import io.simplepix.payment.application.port.out.PixKeyRepository;
import io.simplepix.payment.domain.event.PaymentCompleted;
import io.simplepix.payment.domain.event.PaymentFailed;
import io.simplepix.payment.domain.event.PaymentInitiated;
import io.simplepix.payment.domain.exception.PixKeyNotFoundException;
import io.simplepix.payment.domain.model.Account;
import io.simplepix.payment.domain.model.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService implements InitiatePaymentUseCase {

    private final AccountRepository accountRepository;
    private final PixKeyRepository pixKeyRepository;
    private final PaymentRepository paymentRepository;
    private final DomainEventPublisher eventPublisher;

    public PaymentService(AccountRepository accountRepository,
                          PixKeyRepository pixKeyRepository,
                          PaymentRepository paymentRepository,
                          DomainEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.pixKeyRepository = pixKeyRepository;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public PaymentResult initiate(InitiatePaymentCommand command) {

        paymentRepository.findByIdempotencyKey(command.idempotencyKey())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Payment already processed for idempotency key: " + command.idempotencyKey());
                });

        UUID targetAccountId = pixKeyRepository.findAccountIdByKeyValue(command.targetPixKeyValue())
                .orElseThrow(() -> new PixKeyNotFoundException(command.targetPixKeyValue()));

        Payment payment = Payment.initiate(
                command.sourceAccountId(),
                pixKeyRepository.findByValue(command.targetPixKeyValue())
                        .orElseThrow(() -> new PixKeyNotFoundException(command.targetPixKeyValue())),
                command.amount(),
                command.idempotencyKey()
        );

        try {
            Account sourceAccount = accountRepository.findByIdForUpdate(command.sourceAccountId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Account not found: " + command.sourceAccountId()));

            sourceAccount.debit(command.amount());
            accountRepository.save(sourceAccount);

            Account targetAccount = accountRepository.findByIdForUpdate(targetAccountId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Target account not found: " + targetAccountId));

            targetAccount.credit(command.amount());
            accountRepository.save(targetAccount);

            payment.complete();
            paymentRepository.save(payment);

            eventPublisher.publish(PaymentInitiated.of(
                    payment.id(), payment.sourceAccountId(), command.targetPixKeyValue(), payment.amount()));
            eventPublisher.publish(PaymentCompleted.of(payment.id()));

        } catch (RuntimeException ex) {
            payment.fail(ex.getMessage());
            paymentRepository.save(payment);
            eventPublisher.publish(PaymentFailed.of(payment.id(), ex.getMessage()));
            throw ex;
        }

        return new PaymentResult(payment.id(), payment.status().name());
    }
}