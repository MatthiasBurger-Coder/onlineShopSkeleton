package de.burger.it.infrastructure.cart.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.state.CartStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartStatusAssignmentAdapterTest {

    private CartStatusAssignmentAdapter adapter;
    private Cart cart;
    private UUID cartId;

    @BeforeEach
    void setUp() {
        adapter = new CartStatusAssignmentAdapter();
        cartId = UUID.randomUUID();
        cart = new Cart(cartId);
    }

    // GreenPath Tests

    @Test
    void assign_shouldStoreAssignment() {
        // When
        adapter.assign(cart, CartStateType.CREATED);
        
        // Then
        CartStateType state = adapter.findBy(cartId);
        assertEquals(CartStateType.CREATED, state);
    }

    @Test
    void assign_shouldUpdateExistingAssignment() {
        // Given
        adapter.assign(cart, CartStateType.CREATED);
        
        // When
        adapter.assign(cart, CartStateType.ACTIVE);
        
        // Then
        CartStateType state = adapter.findBy(cartId);
        assertEquals(CartStateType.ACTIVE, state);
    }

    @Test
    void findBy_shouldReturnCorrectState() {
        // Given
        adapter.assign(cart, CartStateType.CLOSED);
        
        // When
        CartStateType state = adapter.findBy(cartId);
        
        // Then
        assertEquals(CartStateType.CLOSED, state);
    }

    // RedPath Tests

    @Test
    void assign_whenCartIsNull_shouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(null, CartStateType.CREATED));
    }

    @Test
    void assign_whenStateIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(cart, null));
    }

    @Test
    void findBy_whenCartIdIsNull_shouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> adapter.findBy(null));
    }

    @Test
    void findBy_whenCartIdNotFound_shouldReturnNull() {
        // When
        CartStateType state = adapter.findBy(UUID.randomUUID());

        // Then
        assertNull(state);
    }
}