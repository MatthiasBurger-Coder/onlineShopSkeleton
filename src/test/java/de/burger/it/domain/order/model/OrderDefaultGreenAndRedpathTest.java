package de.burger.it.domain.order.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDefaultGreenAndRedpathTest {

    @Test
    @DisplayName("Greenpath: OrderDefault should expose id, be non-null object, and respect record equality")
    void orderDefault_Greenpath() {
        UUID id = UUID.randomUUID();
        Order order = new OrderDefault(id);

        // id accessor
        assertEquals(id, order.id());
        // default method from interface
        assertFalse(order.isNull());

        // record semantics: equals/hashCode and toString
        OrderDefault same = new OrderDefault(id);
        assertEquals(order, same);
        assertEquals(order.hashCode(), same.hashCode());
        assertTrue(order.toString().contains(id.toString()));
    }

    @Test
    @DisplayName("Redpath: OrderDefault equals should handle null, unrelated type, and different id")
    void orderDefault_Redpath() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        OrderDefault order1 = new OrderDefault(id1);
        OrderDefault orderDifferent = new OrderDefault(id2);

        assertNotEquals(null, order1);
        assertNotEquals(new Object(), order1);
        assertNotEquals(order1, orderDifferent);
    }
}
