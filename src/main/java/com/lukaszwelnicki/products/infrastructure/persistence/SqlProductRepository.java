package com.lukaszwelnicki.products.infrastructure.persistence;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductId;
import com.lukaszwelnicki.products.domain.model.ProductRepository;
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

@Repository
class SqlProductRepository implements ProductRepository {
    private final JpaProductRepository productRepository;
    private final EntityMapper entityMapper;

    public SqlProductRepository(JpaProductRepository productRepository, EntityMapper entityMapper) {
        this.productRepository = productRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Option<Product> findById(ProductId productId) {
        return Option.ofOptional(productRepository.findById(productId.value())
                .map(entityMapper::from));
    }

    @Override
    public Product save(Product product) {
        var storedEntity = productRepository.save(entityMapper.from(product));
        return entityMapper.from(storedEntity);
    }

    @Override
    public void delete(ProductId productId) {
        productRepository.deleteById(productId.value());
    }
}
