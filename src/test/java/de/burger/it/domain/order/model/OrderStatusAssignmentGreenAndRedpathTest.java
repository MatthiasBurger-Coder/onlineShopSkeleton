package de.burger.it.domain.order.model;

import de.burger.it.domain.order.state.OrderStateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusAssignmentGreenAndRedpathTest {

    @Test
    @DisplayName("Greenpath: OrderStatusAssignment should expose orderId and state and follow record semantics")
    void orderStatusAssignment_Greenpath() {
        UUID orderId = UUID.randomUUID();
        OrderStatusAssignment a = new OrderStatusAssignment(orderId, OrderStateType.NEW);

        assertEquals(orderId, a.orderId());
        assertEquals(OrderStateType.NEW, a.state());

        OrderStatusAssignment same = new OrderStatusAssignment(orderId, OrderStateType.NEW);
        assertEquals(a, same);
        assertEquals(a.hashCode(), same.hashCode());
        assertTrue(a.toString().contains(orderId.toString()));
        assertTrue(a.toString().contains("NEW"));
    }

    @Test
    @DisplayName("Redpath: OrderStatusAssignment inequality when orderId or state differ")
    void orderStatusAssignment_Redpath() {
        UUID orderId = UUID.randomUUID();
        OrderStatusAssignment base = new OrderStatusAssignment(orderId, OrderStateType.NEW);

        assertNotEquals(null, base);
        assertNotEquals(new Object(), base);
        assertNotEquals(base, new OrderStatusAssignment(UUID.randomUUID(), OrderStateType.NEW));
        assertNotEquals(base, new OrderStatusAssignment(orderId, OrderStateType.PAID));
    }
}
