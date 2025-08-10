package de.burger.it.domain.order.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderModelTest {

    @Nested
    class OrderDefaultTests {
        @Test
        void constructor_withValidId_GreenPath() {
            UUID id = UUID.randomUUID();
            Order order = new OrderDefault(id);
            assertEquals(id, order.id());
            assertFalse(order.isNull());
        }

        @Test
        void constructor_withNullId_isAllowed_RedPathDocumentingBehavior() {
            // Current implementation does not guard against null ids (no validation in record)
            Order order = new OrderDefault(null);
            assertNull(order.id());
        }
    }
}
