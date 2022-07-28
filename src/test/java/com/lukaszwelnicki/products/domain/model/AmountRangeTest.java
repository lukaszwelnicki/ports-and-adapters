package com.lukaszwelnicki.products.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AmountRangeTest {

    @Test
    void shouldBeInvalidIfLowerBoundGreaterThanUpperBound() {
        // when
        var result = AmountRange.of(new BigDecimal(12), new BigDecimal(10));

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    @Test
    void shouldBeInvalidIfLowerAmountLessThanZero() {
        // when
        var result = AmountRange.of(new BigDecimal(-1), new BigDecimal(10));

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    @Test
    void shouldBeValid() {
        // when
        var result = AmountRange.of(new BigDecimal(1), new BigDecimal(10));

        // then
        assertThat(result.isValid()).isTrue();
    }
}