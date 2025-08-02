package de.burger.it.infrastructure.order.adapter;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.state.OrderStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusAssignmentAdapterTest {

    private OrderStatusAssignmentAdapter adapter;
    private Order order;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        adapter = new OrderStatusAssignmentAdapter();
        orderId = UUID.randomUUID();
        order = new Order(orderId);
    }

    // GreenPath Tests

    @Test
    void assign_shouldStoreAssignment() {
        // When
        adapter.assign(order, OrderStateType.NEW);
        
        // Then
        OrderStateType state = adapter.findBy(orderId);
        assertEquals(OrderStateType.NEW, state);
    }

    @Test
    void assign_shouldUpdateExistingAssignment() {
        // Given
        adapter.assign(order, OrderStateType.NEW);
        
        // When
        adapter.assign(order, OrderStateType.PAID);
        
        // Then
        OrderStateType state = adapter.findBy(orderId);
        assertEquals(OrderStateType.PAID, state);
    }

    @Test
    void findBy_shouldReturnCorrectState() {
        // Given
        adapter.assign(order, OrderStateType.DELIVERED);
        
        // When
        OrderStateType state = adapter.findBy(orderId);
        
        // Then
        assertEquals(OrderStateType.DELIVERED, state);
    }

    // RedPath Tests

    @Test
    void assign_whenOrderIsNull_shouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(null, OrderStateType.NEW));
    }

    @Test
    void assign_whenStateIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(order, null));
    }

    @Test
    void findBy_whenOrderIdIsNull_shouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> adapter.findBy(null));
    }

    @Test
    void findBy_whenOrderIdNotFound_shouldReturnNull() {
        // When
        OrderStateType state = adapter.findBy(UUID.randomUUID());

        // Then
        assertNull(state);
    }
}