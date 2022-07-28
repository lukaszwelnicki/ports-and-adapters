package com.lukaszwelnicki.products.domain.service;

import com.lukaszwelnicki.products.domain.model.ProductId;

public interface DeleteProduct {
    void deleteProduct(ProductId productId);
}
