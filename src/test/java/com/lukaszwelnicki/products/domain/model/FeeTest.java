package com.lukaszwelnicki.products.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FeeTest {

    @Test
    void shouldBeInvalidIfAmountLessThanZero() {
        // when
        var result = Fee.of("some description", new BigDecimal(-1));

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    @Test
    void shouldBeInvalidIfDescriptionTooShort() {
        // when
        var result = Fee.of("fee", new BigDecimal(10));

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    @Test
    void shouldBeValid() {
        // when
        var result = Fee.of("some description", new BigDecimal(10));

        // then
        assertThat(result.isValid()).isTrue();
    }
}