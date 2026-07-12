package io.simplepix.payment.domain.exception;

public final class PixKeyNotFoundException extends DomainException {

    public PixKeyNotFoundException(String pixKeyValue) {
        super("Pix key not found: " + pixKeyValue);
    }
}