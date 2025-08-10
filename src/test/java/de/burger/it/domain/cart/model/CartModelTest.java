package de.burger.it.domain.cart.model;

import de.burger.it.domain.cart.state.NullCartState;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartModelTest {

    @Nested
    class CartDefaultTests {
        @Test
        void constructor_withValidId_GreenPath() {
            UUID id = UUID.randomUUID();
            CartDefault cart = new CartDefault(id);
            assertEquals(id, cart.id());
            assertFalse(cart.isNull());
        }

        @Test
        void constructor_withNullId_shouldThrow_RedPath() {
            assertThrows(IllegalArgumentException.class, () -> new CartDefault(null));
        }
    }

    @Nested
    class CartNullObjectTests {
        @Test
        void singleton_and_properties_GreenPath() {
            Cart instance1 = CartNullObject.getInstance();
            Cart instance2 = CartNullObject.getInstance();

            assertSame(instance1, instance2, "CartNullObject should be a singleton");
            assertTrue(instance1.isNull());
            assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), instance1.id());
        }

        @Test
        void equals_and_hashCode_behavior_GreenPath() {
            Cart a = CartNullObject.getInstance();
            Cart b = CartNullObject.getInstance();
            assertEquals(a, b, "Two NullCart instances are equal by design");
            assertEquals(a.hashCode(), b.hashCode(), "Hash codes should match for equal objects");
        }

        @Test
        void equals_to_NullCartState_isTrue_GreenPath() {
            Cart a = CartNullObject.getInstance();
            assertEquals(new NullCartState(), a, "Null cart equals NullCartState per implementation");
        }
    }
}
