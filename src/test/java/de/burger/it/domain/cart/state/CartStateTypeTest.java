package de.burger.it.domain.cart.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartStateTypeTest {

    @Test
    @DisplayName("Greenpath: toState() returns matching state implementation")
    void toState_returnsCorrectImplementation_Greenpath() {
        assertInstanceOf(CreatedCartState.class, CartStateType.CREATED.toState());
        assertInstanceOf(ActiveCartState.class, CartStateType.ACTIVE.toState());
        assertInstanceOf(OrderedCartState.class, CartStateType.ORDERED.toState());
        assertInstanceOf(ClosedCartState.class, CartStateType.CLOSED.toState());
        assertInstanceOf(NullCartState.class, CartStateType.NULL_CART_STATE.toState());
    }
}
