package de.burger.it.domain.cart.model;

import de.burger.it.domain.cart.state.CartStateType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartStatusAssignmentTest {

    @Test
    void record_shouldHoldValues_GreenPath() {
        UUID id = UUID.randomUUID();
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now();
        CartStatusAssignment assignment = new CartStatusAssignment(id, CartStateType.ACTIVE, from, to);

        assertEquals(id, assignment.cartId());
        assertEquals(CartStateType.ACTIVE, assignment.state());
        assertEquals(from, assignment.from());
        assertEquals(to, assignment.to());
    }
}
