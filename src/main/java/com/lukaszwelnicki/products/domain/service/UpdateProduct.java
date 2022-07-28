package com.lukaszwelnicki.products.domain.service;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductError;
import io.vavr.control.Either;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.UpdateProductCommand;

public interface UpdateProduct {
    Either<ProductError, Product> updateProduct(UpdateProductCommand updateProductCommand);
}
