package de.burger.it.domain.customer.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerModelTest {

    @Nested
    class CustomerDefaultTests {
        @Test
        void constructor_withValidValues_GreenPath() {
            UUID id = UUID.randomUUID();
            Customer customer = new CustomerDefault(id, "Alice", "alice@example.com");
            assertEquals(id, customer.id());
            assertEquals("Alice", customer.name());
            assertEquals("alice@example.com", customer.email());
            assertFalse(customer.isNull());
        }

        @Test
        void constructor_withNullId_shouldThrow_RedPath() {
            assertThrows(IllegalArgumentException.class, () -> new CustomerDefault(null, "A", "a@b"));
        }

        @Test
        void constructor_withBlankName_shouldThrow_RedPath() {
            assertThrows(IllegalArgumentException.class, () -> new CustomerDefault(UUID.randomUUID(), " ", "a@b"));
            assertThrows(IllegalArgumentException.class, () -> new CustomerDefault(UUID.randomUUID(), null, "a@b"));
        }

        @Test
        void constructor_withBlankEmail_shouldThrow_RedPath() {
            assertThrows(IllegalArgumentException.class, () -> new CustomerDefault(UUID.randomUUID(), "A", " "));
            assertThrows(IllegalArgumentException.class, () -> new CustomerDefault(UUID.randomUUID(), "A", null));
        }
    }

    @Nested
    class CustomerNullObjectTests {
        @Test
        void singleton_and_properties_GreenPath() {
            Customer a = CustomerNullObject.getInstance();
            Customer b = CustomerNullObject.getInstance();
            assertSame(a, b);
            assertTrue(a.isNull());
            assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), a.id());
            assertEquals("", a.name());
            assertEquals("", a.email());
        }
    }
}
