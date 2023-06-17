package com.zkuzniar.workoutplanner;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.zkuzniar.workoutplanner");

        noClasses()
            .that()
            .resideInAnyPackage("com.zkuzniar.workoutplanner.service..")
            .or()
            .resideInAnyPackage("com.zkuzniar.workoutplanner.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.zkuzniar.workoutplanner.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
