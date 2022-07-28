package com.lukaszwelnicki.products.domain.service;

import com.lukaszwelnicki.products.domain.DomainGenerators;
import com.lukaszwelnicki.products.domain.model.ProductError;
import com.lukaszwelnicki.products.domain.model.ProductId;
import com.lukaszwelnicki.products.infrastructure.persistence.InMemoryProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class ProductServiceTest {

    private final InMemoryProductRepository repository = new InMemoryProductRepository();
    private final ProductService productService = new ProductService(repository);

    @Nested
    class GetProduct {

        @Test
        void wontGetProductWhichWasNotStored() {
            // when
            var result = productService.getProduct(ProductId.random());

            // then
            assertThat(result.isLeft()).isTrue();
            assertThat(result.getLeft()).isInstanceOf(ProductError.ProductNotFound.class);
        }

        @Test
        void findsStoredProduct() {
            // given
            var product = DomainGenerators.randomProduct();
            repository.save(product);

            // when
            var result = productService.getProduct(product.id());

            // then
            assertThat(result).singleElement().isEqualTo(product);
        }
    }

    @Nested
    class CreateProduct {

        @Test
        void savesInRepository() {
            // given
            var createProduct = DomainGenerators.randomCreateProduct();

            // when
            var result = productService.createProduct(createProduct);

            // then
            assertThat(result.isRight()).isTrue();
            assertThat(repository.findById(result.get().id())).singleElement().isEqualTo(result.get());
        }

    }

    @Nested
    class UpdateProduct {

        @Test
        void wontUpdateProductWhichWasNotStored() {
            // given
            var updateProduct = DomainGenerators.randomUpdateProduct(ProductId.random());

            // when
            var result = productService.updateProduct(updateProduct);

            // then
            assertThat(result.isLeft()).isTrue();
            assertThat(result.getLeft()).isInstanceOf(ProductError.ProductNotFound.class);
        }

        @Test
        void updatesProduct() {
            // given
            var product = DomainGenerators.randomProduct();
            repository.save(product);
            var updateProduct = DomainGenerators.randomUpdateProduct(product.id());

            // when
            var result = productService.updateProduct(updateProduct);

            // then
            assertThat(result).singleElement().isEqualTo(product.update(updateProduct).get());
        }
    }

    @Nested
    class DeleteProduct {

        @Test
        void noExceptionIsThrownOnDeletingNotExistingProduct() {
            // expect
            assertThatNoException().isThrownBy(() -> productService.deleteProduct(ProductId.random()));
        }

        @Test
        void deletesProduct() {
            // given
            var product = DomainGenerators.randomProduct();
            repository.save(product);

            // when
            productService.deleteProduct(product.id());

            // then
            assertThat(repository.findById(product.id())).isEmpty();
        }
    }
}