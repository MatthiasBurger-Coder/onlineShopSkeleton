package de.burger.it.application.config;

import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("GreenPath: AppConfig component scan should register infrastructure adapter bean")
    void greenPath_componentScanFindsBeans() {
        // Given a Spring context bootstrapped with AppConfig
        context = new AnnotationConfigApplicationContext(AppConfig.class);

        // When retrieving a well-known component from the scanned packages
        OrderStatusAssignmentPort bean = context.getBean(OrderStatusAssignmentPort.class);

        // Then it should exist
        assertNotNull(bean, "OrderStatusAssignmentPort bean should be registered by component scan");
    }

    @Test
    @DisplayName("RedPath: Misconfigured scan should not find beans outside its package")
    void redPath_misconfiguredScanDoesNotFindBeans() {
        // Given a deliberately misconfigured configuration that scans a non-existing package
        context = new AnnotationConfigApplicationContext(BadConfig.class);

        // When / Then - requesting a bean that lives under de.burger.it.* should fail
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> context.getBean(OrderStatusAssignmentPort.class),
                "No bean should be found when scanning the wrong package");
    }

    @Configuration
    @ComponentScan("de.burger.it.nonexistent")
    static class BadConfig {
        // Intentionally empty; scans a package that does not exist in the project
    }
}
