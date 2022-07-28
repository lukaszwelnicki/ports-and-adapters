package com.lukaszwelnicki.products.application;

import com.lukaszwelnicki.products.domain.model.Product;
import com.lukaszwelnicki.products.domain.model.ProductError;
import com.lukaszwelnicki.products.domain.model.ProductId;
import com.lukaszwelnicki.products.domain.service.*;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.*;

@Service
@Transactional
public class ProductApplicationService {
    private final GetProduct getProduct;
    private final CreateProduct createProduct;
    private final UpdateProduct updateProduct;
    private final DeleteProduct deleteProduct;

    public ProductApplicationService(
            GetProduct getProduct, CreateProduct createProduct, UpdateProduct updateProduct,
            DeleteProduct deleteProduct) {
        this.getProduct = getProduct;
        this.createProduct = createProduct;
        this.updateProduct = updateProduct;
        this.deleteProduct = deleteProduct;
    }

    public Either<ProductError, Product> getProduct(ProductId productId) {
        return getProduct.getProduct(productId);
    }

    public Either<ProductError, Product> createProduct(CreateProductCommand createProductCommand) {
        return createProduct.createProduct(createProductCommand);
    }

    public Either<ProductError, Product> updateProduct(UpdateProductCommand updateProductCommand) {
        return updateProduct.updateProduct(updateProductCommand);
    }

    public void deleteProduct(ProductId productId) {
        deleteProduct.deleteProduct(productId);
    }
}
