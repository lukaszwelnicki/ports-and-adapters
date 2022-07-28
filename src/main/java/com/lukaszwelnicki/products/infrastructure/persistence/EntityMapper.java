package com.lukaszwelnicki.products.infrastructure.persistence;

import com.lukaszwelnicki.products.domain.model.AmountRange;
import com.lukaszwelnicki.products.domain.model.Fee;
import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductId;
import io.vavr.collection.List;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
class EntityMapper {
    Product from(ProductEntity entity) {
        return new Product(
                new ProductId(entity.getId()),
                entity.getName(),
                Currency.getInstance(entity.getCurrency()),
                new AmountRange(entity.getLowerBound(), entity.getUpperBound()),
                List.ofAll(entity.getFees().stream().map(this::from).toList())
        );
    }

    ProductEntity from(Product product) {
        return new ProductEntity(
                product.id().value(),
                product.name(),
                product.currency().getCurrencyCode(),
                product.range().getLowerAmount(),
                product.range().getUpperAmount(),
                product.fees().map(this::from).toJavaList()
        );
    }

    private Fee from(FeeEntity entity) {
        return new Fee(entity.getDescription(), entity.getAmount());
    }

    private FeeEntity from(Fee fee) {
        return new FeeEntity(fee.description(), fee.amount());
    }
}
