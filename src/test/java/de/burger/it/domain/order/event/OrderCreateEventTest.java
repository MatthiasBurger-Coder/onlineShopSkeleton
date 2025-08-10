package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderDefault;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreateEventTest {

    @Test
    void constructor_shouldHoldOrder_GreenPath() {
        // Given
        Order order = new OrderDefault(UUID.randomUUID());

        // When
        OrderCreateEvent event = new OrderCreateEvent(order);

        // Then
        assertSame(order, event.order(), "Event should return the same order instance");
    }

    @Test
    void constructor_whenNull_shouldAllowNull_RedPath() {
        // When
        OrderCreateEvent event = new OrderCreateEvent(null);

        // Then
        assertNull(event.order(), "Order may be null in event");
    }
}
