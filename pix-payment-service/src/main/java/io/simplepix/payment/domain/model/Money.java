package io.simplepix.payment.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(long cents, String currency) {

    public Money {
        if (cents < 0) {
            throw new IllegalArgumentException("Money cannot be negative: " + cents);
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO code: " + currency);
        }
    }

    public static Money brl(long cents) {
        return new Money(cents, "BRL");
    }

    public static Money ofReais(BigDecimal reais) {
        long cents = reais.setScale(2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();
        return brl(cents);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.cents + other.cents, currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(this.cents - other.cents, currency);
    }

    public boolean isGreaterThanOrEqualTo(Money other) {
        requireSameCurrency(other);
        return this.cents >= other.cents;
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Cannot operate on different currencies: %s vs %s".formatted(currency, other.currency));
        }
    }

}
