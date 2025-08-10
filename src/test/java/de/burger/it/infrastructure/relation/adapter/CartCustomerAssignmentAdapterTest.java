package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartCustomerAssignmentAdapterTest {

    private CartCustomerAssignmentAdapter adapter;
    private CartDefault cart;
    private CustomerDefault customer;
    private UUID cartId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        adapter = new CartCustomerAssignmentAdapter();
        cartId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        cart = new CartDefault(cartId);
        customer = new CustomerDefault(customerId, "Test Customer", "test@example.com");
    }

    // GreenPath Tests

    @Test
    void assign_shouldStoreAssignment() {
        // When
        adapter.assign(cart, customer);
        
        // Then
        List<CartCustomerAssignment> assignments = adapter.findAllByCustomer(customerId);
        assertEquals(1, assignments.size());
        assertEquals(cartId, assignments.getFirst().cartId());
        assertEquals(customerId, assignments.getFirst().customerId());
    }

    @Test
    void findAllByCart_shouldReturnAssignmentsForCart() {
        // Given
        adapter.assign(cart, customer);
        // Add an unrelated assignment to ensure filtering works
        CartDefault anotherCart = new CartDefault(UUID.randomUUID());
        adapter.assign(anotherCart, customer);
        
        // When
        List<CartCustomerAssignment> assignments = adapter.findAllByCart(cartId);
        
        // Then
        assertEquals(1, assignments.size());
        CartCustomerAssignment only = assignments.getFirst();
        assertEquals(cartId, only.cartId());
        assertEquals(customerId, only.customerId());
    }

    @Test
    void findAllByCustomer_shouldReturnAssignmentsForCustomer() {
        // Given
        adapter.assign(cart, customer);

        // When
        List<CartCustomerAssignment> assignments = adapter.findAllByCustomer(customerId);

        // Then
        assertEquals(1, assignments.size());
        assertEquals(cartId, assignments.getFirst().cartId());
        assertEquals(customerId, assignments.getFirst().customerId());
    }

    @Test
    void assign_multipleAssignments_shouldStoreAllAssignments() {
        // Given
        CartDefault cart2 = new CartDefault(UUID.randomUUID());
        
        // When
        adapter.assign(cart, customer);
        adapter.assign(cart2, customer);
        
        // Then
        List<CartCustomerAssignment> assignments = adapter.findAllByCustomer(customerId);
        assertEquals(2, assignments.size());
    }

    // RedPath Tests

    @Test
    void findAllByCart_whenCartIdIsNull_shouldThrowException() {
        // When/Then
        // Verify that IllegalArgumentException is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.findAllByCart(null));
    }

    @Test
    void findAllByCustomer_whenCustomerIdIsNull_shouldThrowException() {
        // When/Then
        // Verify that IllegalArgumentException is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.findAllByCustomer(null));
    }

    @Test
    void assign_whenCartIsNull_shouldThrowException() {
        // When/Then
        // Verify that IllegalArgumentException is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(null, customer));
    }

    @Test
    void assign_whenCustomerIsNull_shouldThrowException() {
        // When/Then
        // Verify that IllegalArgumentException is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(cart, null));
    }

    @Test
    void findAllByCart_whenNoAssignmentsExist_shouldReturnEmptyList() {
        // When
        List<CartCustomerAssignment> assignments = adapter.findAllByCart(UUID.randomUUID());
        
        // Then
        assertTrue(assignments.isEmpty());
    }

    @Test
    void findAllByCustomer_whenNoAssignmentsExist_shouldReturnEmptyList() {
        // When
        List<CartCustomerAssignment> assignments = adapter.findAllByCustomer(UUID.randomUUID());
        
        // Then
        assertTrue(assignments.isEmpty());
    }
}