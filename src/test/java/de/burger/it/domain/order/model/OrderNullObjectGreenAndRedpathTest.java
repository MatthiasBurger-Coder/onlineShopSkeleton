package de.burger.it.domain.order.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderNullObjectGreenAndRedpathTest {

    @Test
    @DisplayName("Greenpath: OrderNullObject should be singleton with zero UUID and isNull=true")
    void orderNullObject_Greenpath() {
        Order first = OrderNullObject.getInstance();
        Order second = OrderNullObject.getInstance();

        // Singleton identity
        assertSame(first, second);

        // id is the zero UUID and isNull is true
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), first.id());
        assertTrue(first.isNull());
    }

    @Test
    @DisplayName("Redpath: OrderNullObject equality with null and unrelated objects should be false")
    void orderNullObject_Redpath() {
        Order nullOrder = OrderNullObject.getInstance();
        assertNotEquals(nullOrder, null);
        assertNotEquals(nullOrder, new Object());
    }
}
