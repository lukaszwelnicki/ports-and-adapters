package com.lukaszwelnicki.products.domain.model;

import io.vavr.control.Option;

public interface ProductRepository {
    Option<Product> findById(ProductId productId);

    Product save(Product product);

    void delete(ProductId productId);
}
