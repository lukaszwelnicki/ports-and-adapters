package com.lukaszwelnicki.products.domain.model;

import com.google.common.collect.Range;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.math.BigDecimal;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

public record AmountRange(Range<BigDecimal> amountRange) {

    public AmountRange(BigDecimal lowerBound, BigDecimal upperBound) {
        this(Range.closedOpen(lowerBound, upperBound));
    }

    public static Validation<Seq<String>, AmountRange> of(BigDecimal lowerAmount, BigDecimal upperAmount) {
        return Validation.combine(
                lowerAmount.compareTo(upperAmount) < 0 ? valid(lowerAmount) : invalid("Lower amount must be less than upper amount"),
                lowerAmount.signum() >= 0 ? valid(upperAmount) : invalid("Amounts must not be negative")
        ).ap(AmountRange::new);
    }

    public BigDecimal getLowerAmount() {
        return amountRange.lowerEndpoint();
    }

    public BigDecimal getUpperAmount() {
        return amountRange.upperEndpoint();
    }
}
