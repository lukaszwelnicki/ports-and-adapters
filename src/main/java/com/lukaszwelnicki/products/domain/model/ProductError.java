package com.lukaszwelnicki.products.domain.model;

public sealed interface ProductError {
    record ValidationError(String error) implements ProductError {
    }

    record ProductNotFound(String error) implements ProductError {
        public static ProductNotFound of(ProductId id) {
            return new ProductNotFound("Product not found: %s".formatted(id));
        }
    }
}
