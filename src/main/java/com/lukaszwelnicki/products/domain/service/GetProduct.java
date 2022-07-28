package com.lukaszwelnicki.products.domain.service;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductError;
import com.lukaszwelnicki.products.domain.model.ProductId;
import io.vavr.control.Either;

public interface GetProduct {
    Either<ProductError, Product> getProduct(ProductId productId);
}
