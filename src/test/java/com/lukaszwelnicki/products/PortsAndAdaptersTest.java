package com.lukaszwelnicki.products;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class PortsAndAdaptersTest {
    private final JavaClasses classes = new ClassFileImporter().withImportOption(new DoNotIncludeTests())
            .importPackages("com.lukaszwelnicki.products");

    @Test
    void shouldFollowPortsAndAdaptersRules() {
        onionArchitecture()
                .domainModels("com.lukaszwelnicki.products.domain.model..")
                .domainServices("com.lukaszwelnicki.products.domain.service..")
                .applicationServices("com.lukaszwelnicki.products.application..")
                .adapter("persistence", "com.lukaszwelnicki.products.infrastructure.persistence..")
                .adapter("rest", "com.lukaszwelnicki.products.infrastructure.rest..")
                .check(classes);
    }
}
