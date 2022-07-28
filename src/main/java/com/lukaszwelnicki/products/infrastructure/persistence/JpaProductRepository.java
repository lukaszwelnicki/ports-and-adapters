package com.lukaszwelnicki.products.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface JpaProductRepository extends CrudRepository<ProductEntity, UUID> {
}
