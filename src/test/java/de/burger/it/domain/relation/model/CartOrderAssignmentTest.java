package de.burger.it.domain.relation.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartOrderAssignmentTest {

    @Test
    void record_shouldHoldValues_GreenPath() {
        // Given
        UUID cartId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        // When
        CartOrderAssignment assignment = new CartOrderAssignment(cartId, orderId);

        // Then
        assertEquals(cartId, assignment.cartId());
        assertEquals(orderId, assignment.orderId());
    }

    @Test
    void record_whenNulls_shouldAllowNulls_RedPath() {
        // When
        CartOrderAssignment assignment = new CartOrderAssignment(null, null);

        // Then
        assertNull(assignment.cartId());
        assertNull(assignment.orderId());
    }
}
