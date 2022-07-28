package com.lukaszwelnicki.products.infrastructure.rest;

import com.lukaszwelnicki.products.domain.model.Fee;
import com.lukaszwelnicki.products.domain.model.Product;
import io.vavr.collection.Seq;

record ProductResponse(
        String name,
        String currency,
        String lowerAmount,
        String upperAmount,
        Seq<FeeResponse> fees) {
    static ProductResponse from(Product product) {
        return new ProductResponse(
                product.name(),
                product.currency().getCurrencyCode(),
                product.range().getLowerAmount().toPlainString(),
                product.range().getUpperAmount().toPlainString(),
                product.fees().map(FeeResponse::from)
        );
    }
}

record FeeResponse(String description, String amount) {
    static FeeResponse from(Fee fee) {
        return new FeeResponse(fee.description(), fee.amount().toPlainString());
    }
}
