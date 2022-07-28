package com.lukaszwelnicki.products.domain;

import com.lukaszwelnicki.products.domain.model.*;
import io.vavr.collection.List;

import java.math.BigDecimal;
import java.util.Currency;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class DomainGenerators {

    public static CreateProductCommand randomCreateProduct() {
        return new CreateProductCommand(
                randomAlphanumeric(10),
                Currency.getInstance("EUR"),
                randomAmountRange(),
                List.fill(nextInt(1, 4), DomainGenerators::randomFee)
        );
    }

    public static Product randomProduct() {
        return new Product(
                ProductId.random(),
                randomAlphanumeric(10),
                Currency.getInstance("EUR"),
                randomAmountRange(),
                List.fill(nextInt(1, 4), DomainGenerators::randomFee)
        );
    }

    public static UpdateProductCommand randomUpdateProduct(ProductId productId) {
        return new UpdateProductCommand(
                productId,
                randomAlphanumeric(10),
                Currency.getInstance("EUR"),
                randomAmountRange(),
                List.fill(nextInt(1, 4), DomainGenerators::randomFee)
        );
    }

    public static Fee randomFee() {
        return new Fee(randomAlphanumeric(15), randomBigDecimal(1, 10));
    }

    private static AmountRange randomAmountRange() {
        return new AmountRange(randomBigDecimal(0, 1000), randomBigDecimal(2000, 10000));
    }

    private static BigDecimal randomBigDecimal(int from, int to) {
        return new BigDecimal(nextInt(from, to));
    }
}
