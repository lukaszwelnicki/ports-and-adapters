package com.lukaszwelnicki.products.infrastructure.rest;

import io.vavr.collection.List;
import lombok.With;

record ProductRequest(
        String name,
        @With String currency,
        @With String lowerAmount,
        @With String upperAmount,
        @With List<FeeRequest> fees) {
}

record FeeRequest(String description, String amount) {
}
