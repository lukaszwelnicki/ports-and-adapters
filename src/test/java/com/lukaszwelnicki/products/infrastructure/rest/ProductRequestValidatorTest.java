package com.lukaszwelnicki.products.infrastructure.rest;

import com.lukaszwelnicki.products.domain.model.AmountRange;
import com.lukaszwelnicki.products.domain.model.Fee;
import com.lukaszwelnicki.products.domain.model.ProductCommand;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestValidatorTest {

    public static final String PRODUCT_NAME = "Product name";
    public static final String CURRENCY = "EUR";
    public static final String LOWER_AMOUNT = "0.00";
    public static final String UPPER_AMOUNT = "10.00";
    public static final String SOME_FEE_DESCRIPTION = "Some fee";
    public static final String OTHER_FEE_DESCRIPTION = "Other fee";
    public static final String SOME_FEE_AMOUNT = "2.00";
    public static final String OTHER_FEE_AMOUNT = "0.99";
    private static final ProductRequest VALID_REQUEST = new ProductRequest(
            PRODUCT_NAME,
            CURRENCY,
            LOWER_AMOUNT,
            UPPER_AMOUNT,
            List.of(new FeeRequest(SOME_FEE_DESCRIPTION, SOME_FEE_AMOUNT), new FeeRequest(OTHER_FEE_DESCRIPTION, OTHER_FEE_AMOUNT))
    );
    private static final ProductCommand.CreateProductCommand VALID_COMMAND = new ProductCommand.CreateProductCommand(
            PRODUCT_NAME,
            Currency.getInstance(CURRENCY),
            new AmountRange(new BigDecimal(LOWER_AMOUNT), new BigDecimal(UPPER_AMOUNT)),
            List.of(new Fee(SOME_FEE_DESCRIPTION, new BigDecimal(SOME_FEE_AMOUNT)),
                    new Fee(OTHER_FEE_DESCRIPTION, new BigDecimal(OTHER_FEE_AMOUNT)))
    );

    @Test
    void requestShouldBeValid() {
        // when
        var result = ProductRequestValidator.validate(VALID_REQUEST);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get()).isEqualTo(VALID_COMMAND);
    }

    @Test
    void currencyShouldBeInvalid() {
        // given
        var request = VALID_REQUEST.withCurrency("abcd");

        // when
        var result = ProductRequestValidator.validate(request);

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidAmounts")
    void amountShouldBeInvalid(String requestedAmount) {
        // given
        var request = VALID_REQUEST.withLowerAmount(requestedAmount);

        // when
        var result = ProductRequestValidator.validate(request);

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    private static Stream<Arguments> invalidAmounts() {
        return Stream.of(
                Arguments.of("1.000", "-1.00", "1E9")
        );
    }

    @Test
    void feesShouldBeInvalid() {
        // given
        var request = VALID_REQUEST.withFees(List.of(
                new FeeRequest("Some invalid fee", "-0.01"),
                new FeeRequest("Bad", "10.00")));

        // when
        var result = ProductRequestValidator.validate(request);

        // then
        assertThat(result.isInvalid()).isTrue();
    }

    @Test
    void shouldAggregateMultipleErrors() {
        // given
        var request = VALID_REQUEST.withCurrency("abcd")
                .withLowerAmount("0.123")
                .withFees(List.of(new FeeRequest("Bad", "1e9")));

        // when
        var result = ProductRequestValidator.validate(request);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).hasSize(3);
    }
}