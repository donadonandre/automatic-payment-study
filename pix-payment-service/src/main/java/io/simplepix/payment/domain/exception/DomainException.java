package io.simplepix.payment.domain.exception;

public sealed class DomainException extends RuntimeException
        permits InsufficientFundsException, PixKeyNotFoundException {

    protected DomainException(String message) {
        super(message);
    }
}