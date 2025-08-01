package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartCustomerAssignmentAdapterTest {

    private CartCustomerAssignmentAdapter adapter;
    private Cart cart;
    private Customer customer;
    private UUID cartId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        adapter = new CartCustomerAssignmentAdapter();
        cartId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        cart = new Cart(cartId);
        customer = new Customer(customerId, "Test Customer", "test@example.com");
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
    void findAllByCard_shouldReturnAssignmentsForCart() {
        // Given
        adapter.assign(cart, customer);
        
        // When
        List<CartCustomerAssignment> assignments = adapter.findAllByCard(cartId);
        
        // Then
        assertEquals(1, assignments.size());
        assertEquals(cartId, assignments.getFirst().cartId());
        assertEquals(customerId, assignments.getFirst().customerId());
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
        Cart cart2 = new Cart(UUID.randomUUID());
        
        // When
        adapter.assign(cart, customer);
        adapter.assign(cart2, customer);
        
        // Then
        List<CartCustomerAssignment> assignments = adapter.findAllByCustomer(customerId);
        assertEquals(2, assignments.size());
    }

    // RedPath Tests

    @Test
    void findAllByCard_whenCartIdIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> adapter.findAllByCard(null));
    }

    @Test
    void findAllByCustomer_whenCustomerIdIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> adapter.findAllByCustomer(null));
    }

    @Test
    void assign_whenCartIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> adapter.assign(null, customer));
    }

    @Test
    void assign_whenCustomerIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> adapter.assign(cart, null));
    }

    @Test
    void findAllByCard_whenNoAssignmentsExist_shouldReturnEmptyList() {
        // When
        List<CartCustomerAssignment> assignments = adapter.findAllByCard(UUID.randomUUID());
        
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