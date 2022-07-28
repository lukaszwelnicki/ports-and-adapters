package com.lukaszwelnicki.products.domain.model;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static com.lukaszwelnicki.products.domain.DomainGenerators.*;
import static com.lukaszwelnicki.products.domain.model.ProductError.ValidationError;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void shouldCreateProduct() {
        // given
        var createProductCommand = randomCreateProduct();

        // when
        var result = Product.create(createProductCommand);

        // then
        assertThat(result.isRight()).isTrue();
    }

    @Test
    void shouldUpdateProduct() {
        // given
        var product = randomProduct();
        var updateProduct = randomUpdateProduct(product.id());

        // when
        var result = product.update(updateProduct);

        // then
        assertThat(result.isRight()).isTrue();
    }

    @Test
    void shouldNotCreateProductIfTooManyFees() {
        // given
        var createProductCommand = randomCreateProduct()
                .withFees(List.fill(nextInt(4, 10), randomFee()));

        // when
        var result = Product.create(createProductCommand);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(new ValidationError("Can not apply more than three fees"));
    }

    @Test
    void shouldNotUpdateProductIfTooManyFees() {
        // given
        var product = randomProduct();
        var updateProductCommand = randomUpdateProduct(product.id())
                .withFees(List.fill(nextInt(4, 10), randomFee()));

        // when
        var result = product.update(updateProductCommand);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(new ValidationError("Can not apply more than three fees"));
    }
}