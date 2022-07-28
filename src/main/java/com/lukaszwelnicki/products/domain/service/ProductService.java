package com.lukaszwelnicki.products.domain.service;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductError;
import com.lukaszwelnicki.products.domain.model.ProductId;
import com.lukaszwelnicki.products.domain.model.ProductRepository;
import io.vavr.control.Either;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.CreateProductCommand;
import static com.lukaszwelnicki.products.domain.model.ProductCommand.UpdateProductCommand;
import static com.lukaszwelnicki.products.domain.model.ProductError.ProductNotFound;

public class ProductService implements CreateProduct, UpdateProduct, DeleteProduct, GetProduct {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Either<ProductError, Product> getProduct(ProductId productId) {
        return repository.findById(productId)
                .toEither(() -> ProductNotFound.of(productId));
    }

    @Override
    public Either<ProductError, Product> createProduct(CreateProductCommand createProductCommand) {
        return Product.create(createProductCommand)
                .map(repository::save);
    }

    @Override
    public Either<ProductError, Product> updateProduct(UpdateProductCommand updateProductCommand) {
        return getProduct(updateProductCommand.id())
                .flatMap(product -> product.update(updateProductCommand))
                .map(repository::save);
    }

    @Override
    public void deleteProduct(ProductId productId) {
        repository.delete(productId);
    }
}
