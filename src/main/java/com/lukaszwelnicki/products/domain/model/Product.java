package com.lukaszwelnicki.products.domain.model;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.With;

import java.util.Currency;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.*;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

public record Product(ProductId id,
                      @With(AccessLevel.PRIVATE) String name,
                      @With(AccessLevel.PRIVATE) Currency currency,
                      @With(AccessLevel.PRIVATE) AmountRange range,
                      @With(AccessLevel.PRIVATE) Seq<Fee> fees) {

    public static Either<ProductError, Product> create(CreateProductCommand createProductCommand) {
        return validateFees(createProductCommand.fees())
                .map(validFees -> new Product(
                        ProductId.random(),
                        createProductCommand.name(),
                        createProductCommand.currency(),
                        createProductCommand.amountRange(),
                        validFees));
    }

    public Either<ProductError, Product> update(UpdateProductCommand updateProductCommand) {
        return validateFees(updateProductCommand.fees())
                .map(validFees -> this
                        .withName(updateProductCommand.name())
                        .withCurrency(updateProductCommand.currency())
                        .withRange(updateProductCommand.amountRange())
                        .withFees(validFees));
    }

    private static Either<ProductError, Seq<Fee>> validateFees(Seq<Fee> fees) {
        if (fees.size() > 3) {
            return left(new ProductError.ValidationError("Can not apply more than three fees"));
        }
        return right(fees);
    }
}
