package com.lukaszwelnicki.products.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszwelnicki.products.application.ProductApplicationService;
import com.lukaszwelnicki.products.domain.model.ProductError;
import com.lukaszwelnicki.products.domain.model.ProductId;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.CreateProductCommand;
import static com.lukaszwelnicki.products.domain.model.ProductCommand.UpdateProductCommand;
import static com.lukaszwelnicki.products.domain.model.ProductError.ProductNotFound;
import static com.lukaszwelnicki.products.domain.model.ProductError.ValidationError;
import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductApplicationService applicationService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    ResponseEntity<String> get(@PathVariable String id) {
        return ProductRequestValidator.validateProductId(id)
                .map(this::getProduct)
                .getOrElseGet(this::handleValidationError);
    }

    @PostMapping
    ResponseEntity<String> create(@RequestBody ProductRequest productRequest) {
        return ProductRequestValidator.validate(productRequest)
                .map(this::createProduct)
                .getOrElseGet(this::handleValidationErrors);
    }

    @PutMapping("/{id}")
    ResponseEntity<String> update(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        return ProductRequestValidator.validate(id, productRequest)
                .map(this::updateProduct)
                .getOrElseGet(this::handleValidationErrors);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable String id) {
        return ProductRequestValidator.validateProductId(id)
                .map(this::deleteProduct)
                .getOrElseGet(this::handleValidationError);
    }

    private ResponseEntity<String> getProduct(ProductId productId) {
        return applicationService.getProduct(productId)
                .map(ProductResponse::from)
                .map(response -> ok().body(toJson(response)))
                .getOrElseGet(this::handleProductError);
    }

    private ResponseEntity<String> createProduct(CreateProductCommand createProductCommand) {
        return applicationService.createProduct(createProductCommand)
                .map(ProductResponse::from)
                .map(response -> ok().body(toJson(response)))
                .getOrElseGet(this::handleProductError);
    }

    private ResponseEntity<String> updateProduct(UpdateProductCommand updateProductCommand) {
        return applicationService.updateProduct(updateProductCommand)
                .map(ProductResponse::from)
                .map(response -> ok().body(toJson(response)))
                .getOrElseGet(this::handleProductError);
    }

    private ResponseEntity<String> deleteProduct(ProductId productId) {
        applicationService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<String> handleProductError(ProductError productError) {
        return Match(productError).of(
                Case($(instanceOf(ValidationError.class)), validationError -> badRequest().body(toJson(validationError.error()))),
                Case($(instanceOf(ProductNotFound.class)), notFound -> ResponseEntity.notFound().build())
        );
    }

    private ResponseEntity<String> handleValidationErrors(Seq<String> errors) {
        return badRequest().body(toJson(errors));
    }

    private ResponseEntity<String> handleValidationError(String error) {
        return badRequest().body(toJson(error));
    }

    private String toJson(Object obj) {
        return Try.of(() -> objectMapper.writeValueAsString(obj)).get();
    }
}
