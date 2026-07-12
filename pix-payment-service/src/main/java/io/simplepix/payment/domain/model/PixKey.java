package io.simplepix.payment.domain.model;

import java.util.regex.Pattern;

public record PixKey(String value, PixKeyType type) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{14}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+55\\d{10,11}$");

    public PixKey {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pix key value cannot be blank");
        }
        if (!matchesType(value, type)) {
            throw new IllegalArgumentException(
                    "Value '%s' does not match key type %s".formatted(value, type));
        }
    }

    private static boolean matchesType(String value, PixKeyType type) {
        return switch (type) {
            case CPF -> CPF_PATTERN.matcher(value).matches();
            case CNPJ -> CNPJ_PATTERN.matcher(value).matches();
            case EMAIL -> EMAIL_PATTERN.matcher(value).matches();
            case PHONE -> PHONE_PATTERN.matcher(value).matches();
            case RANDOM -> value.length() == 36;
        };
    }

}
