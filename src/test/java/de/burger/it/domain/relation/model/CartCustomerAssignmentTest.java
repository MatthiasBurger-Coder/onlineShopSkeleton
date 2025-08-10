package de.burger.it.domain.relation.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartCustomerAssignmentTest {

    @Test
    void record_shouldHoldValues_GreenPath() {
        // Given
        UUID cartId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        // When
        CartCustomerAssignment assignment = new CartCustomerAssignment(cartId, customerId);

        // Then
        assertEquals(cartId, assignment.cartId());
        assertEquals(customerId, assignment.customerId());
    }

    @Test
    void record_whenNulls_shouldAllowNulls_RedPath() {
        // When
        CartCustomerAssignment assignment = new CartCustomerAssignment(null, null);

        // Then
        assertNull(assignment.cartId());
        assertNull(assignment.customerId());
    }
}
