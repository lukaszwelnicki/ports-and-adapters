package com.lukaszwelnicki;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lukaszwelnicki.products.domain.model.ProductRepository;
import com.lukaszwelnicki.products.domain.service.ProductService;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpringConfiguration {

    @Bean
    ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }

    @Bean
    ObjectMapper jsonWriter() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new VavrModule(new VavrModule.Settings()
                        .useOptionInPlainFormat(true)
                        .deserializeNullAsEmptyCollection(true)))
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }}
