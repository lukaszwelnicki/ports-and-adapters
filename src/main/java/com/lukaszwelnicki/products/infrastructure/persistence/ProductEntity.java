package com.lukaszwelnicki.products.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
@Getter
class ProductEntity {
    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private String name;
    private String currency;
    private BigDecimal lowerBound;
    private BigDecimal upperBound;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_fees", joinColumns = @JoinColumn(name = "fk_product"))
    private List<FeeEntity> fees;
}
