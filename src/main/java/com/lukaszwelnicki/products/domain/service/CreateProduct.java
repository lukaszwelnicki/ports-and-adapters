package com.lukaszwelnicki.products.domain.service;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductError;
import io.vavr.control.Either;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.*;

public interface CreateProduct {
    Either<ProductError, Product> createProduct(CreateProductCommand createProductCommand);
}
