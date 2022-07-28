package com.lukaszwelnicki.products.domain.model;

import java.util.UUID;

public record ProductId(UUID value) {

    public static ProductId random() {
        return new ProductId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
