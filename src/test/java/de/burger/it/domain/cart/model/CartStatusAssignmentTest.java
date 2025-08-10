package de.burger.it.domain.cart.model;

import de.burger.it.domain.cart.state.CartStateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartStatusAssignmentTest {

    @Test
    @DisplayName("Greenpath: record stores values and exposes them via accessors")
    void constructor_and_accessors_Greenpath() {
        UUID cartId = UUID.randomUUID();
        CartStateType state = CartStateType.ACTIVE;
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        CartStatusAssignment assignment = new CartStatusAssignment(cartId, state, from, to);

        assertEquals(cartId, assignment.cartId());
        assertEquals(state, assignment.state());
        assertEquals(from, assignment.from());
        assertEquals(to, assignment.to());
    }

    @Test
    @DisplayName("Green/Red: equals and hashCode contract")
    void equals_and_hashCode_contract() {
        UUID cartId = UUID.randomUUID();
        LocalDateTime from = LocalDateTime.now().minusHours(2);
        LocalDateTime to = LocalDateTime.now();

        CartStatusAssignment a = new CartStatusAssignment(cartId, CartStateType.CREATED, from, to);
        CartStatusAssignment b = new CartStatusAssignment(cartId, CartStateType.CREATED, from, to);
        CartStatusAssignment c = new CartStatusAssignment(cartId, CartStateType.CLOSED, from, to);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }
}
