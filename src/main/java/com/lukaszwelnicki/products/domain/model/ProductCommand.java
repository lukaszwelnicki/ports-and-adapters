package com.lukaszwelnicki.products.domain.model;

import io.vavr.collection.Seq;
import lombok.With;

import java.util.Currency;

public sealed interface ProductCommand {

    @With
    record CreateProductCommand(String name,
                                Currency currency,
                                AmountRange amountRange,
                                Seq<Fee> fees) implements ProductCommand {
    }

    @With
    record UpdateProductCommand(ProductId id,
                                String name,
                                Currency currency,
                                AmountRange amountRange,
                                Seq<Fee> fees) implements ProductCommand {

    }
}
