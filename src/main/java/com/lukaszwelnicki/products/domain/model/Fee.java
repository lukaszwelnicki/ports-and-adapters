package com.lukaszwelnicki.products.domain.model;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.math.BigDecimal;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

public record Fee(String description, BigDecimal amount) {

    public static Validation<Seq<String>, Fee> of(String description, BigDecimal amount) {
        return Validation.combine(validateDescription(description), validateAmount(amount)).ap(Fee::new);
    }

    private static Validation<String, BigDecimal> validateAmount(BigDecimal amount) {
        return amount.signum() < 0
                ? invalid("Fee amount must be greater or equals to 0, but was: %s".formatted(amount))
                : valid(amount);
    }

    private static Validation<String, String> validateDescription(String description) {
        return description.length() < 5
                ? invalid("Fee description must have at least five characters")
                : valid(description);
    }
}
