package io.simplepix.payment.adapter.in.web;

import io.simplepix.payment.adapter.in.web.dto.ErrorResponse;
import io.simplepix.payment.domain.exception.DomainException;
import io.simplepix.payment.domain.exception.InsufficientFundsException;
import io.simplepix.payment.domain.exception.PixKeyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        HttpStatus status = switch (ex) {
            case InsufficientFundsException e -> HttpStatus.UNPROCESSABLE_ENTITY;
            case PixKeyNotFoundException e -> HttpStatus.NOT_FOUND;
            // Unreachable: DomainException is sealed to exactly these two final subtypes.
            // Kept only as a safety net in case the IDE/compiler toolchain disagrees on exhaustiveness.
            default -> throw new IllegalStateException("Unmapped domain exception: " + ex.getClass());
        };
        return ResponseEntity.status(status)
                .body(ErrorResponse.of(status.value(), status.getReasonPhrase(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", message));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage()));
    }
}