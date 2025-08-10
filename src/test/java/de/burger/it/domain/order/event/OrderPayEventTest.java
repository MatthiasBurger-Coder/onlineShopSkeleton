package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderDefault;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderPayEventTest {

    @Test
    void constructor_shouldHoldOrder_GreenPath() {
        // Given
        Order order = new OrderDefault(UUID.randomUUID());

        // When
        OrderPayEvent event = new OrderPayEvent(order);

        // Then
        assertSame(order, event.order(), "Event should return the same order instance");
    }

    @Test
    void constructor_whenNull_shouldAllowNull_RedPath() {
        // When
        OrderPayEvent event = new OrderPayEvent(null);

        // Then
        assertNull(event.order(), "Order may be null in event");
    }
}
