package com.lukaszwelnicki.products.infrastructure.persistence;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductId;
import com.lukaszwelnicki.products.domain.model.ProductRepository;
import io.vavr.control.Option;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> map = new ConcurrentHashMap<>();

    @Override
    public Option<Product> findById(ProductId productId) {
        return Option.of(map.get(productId));
    }

    @Override
    public Product save(Product product) {
        map.put(product.id(), product);
        return product;
    }

    @Override
    public void delete(ProductId productId) {
        map.remove(productId);
    }
}
