package io.simplepix.payment.domain.model;

import io.simplepix.payment.domain.exception.InsufficientFundsException;

import java.time.Instant;
import java.util.UUID;

public final class Account {

    private final UUID id;
    private final String holderName;
    private final String documentNumber;
    private Money balance;
    private final Instant createdAt;
    private Instant updatedAt;

    private Account(UUID id, String holderName, String documentNumber, Money balance,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.holderName = holderName;
        this.documentNumber = documentNumber;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account open(String holderName, String documentNumber) {
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Holder name cannot be blank");
        }
        if (documentNumber == null || (documentNumber.length() != 11 && documentNumber.length() != 14)) {
            throw new IllegalArgumentException("Document number must be a valid CPF or CNPJ");
        }
        Instant now = Instant.now();
        return new Account(UUID.randomUUID(), holderName, documentNumber, Money.brl(0), now, now);
    }

    public static Account reconstitute(UUID id, String holderName, String documentNumber,
                                       Money balance, Instant createdAt, Instant updatedAt) {
        return new Account(id, holderName, documentNumber, balance, createdAt, updatedAt);
    }

    public void debit(Money amount) {
        if (!balance.isGreaterThanOrEqualTo(amount)) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        this.balance = balance.subtract(amount);
        this.updatedAt = Instant.now();
    }

    public void credit(Money amount) {
        this.balance = balance.add(amount);
        this.updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String holderName() { return holderName; }
    public String documentNumber() { return documentNumber; }
    public Money balance() { return balance; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}