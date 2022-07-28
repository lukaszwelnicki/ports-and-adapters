package com.lukaszwelnicki.products.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import static com.lukaszwelnicki.products.domain.DomainGenerators.randomProduct;
import static org.assertj.core.api.Assertions.assertThat;

class EntityMapperTest {

    private final EntityMapper entityMapper = new EntityMapper();

    @Test
    void shouldMapProduct() {
        // given
        var product = randomProduct();

        // when
        var entity = entityMapper.from(product);
        var result = entityMapper.from(entity);

        // then
        assertThat(result).isEqualTo(product);
    }
}