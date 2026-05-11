package com.axinger.modulith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Spring Modulith 架构测试
 */
class ModulithArchitectureTest {

    private final ApplicationModules modules = ApplicationModules.of(ModulithApplication.class);

    /**
     * 验证模块依赖关系是否符合配置
     */
    @Test
    void shouldRespectModuleDependencies() {
        modules.verify();
    }

    /**
     * 验证分层架构
     */
    @Test
    void shouldHaveCorrectLayering() {
        JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.axinger");

        layeredArchitecture()
            .consideringAllDependencies()
            .layer("Web").definedBy("..web..")
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .whereLayer("Web").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Web", "Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
            .check(importedClasses);
    }
}
