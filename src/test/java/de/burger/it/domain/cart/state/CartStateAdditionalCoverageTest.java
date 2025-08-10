package de.burger.it.domain.cart.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartStateAdditionalCoverageTest {

    @Test
    @DisplayName("Greenpath: Created.close() transitions to ClosedCartState")
    void created_close_transitionsToClosed_Greenpath() {
        CartState created = new CreatedCartState();
        CartState closed = created.close();
        assertInstanceOf(ClosedCartState.class, closed);
        assertEquals(CartStateType.CLOSED, closed.code());
    }

    @Test
    @DisplayName("Greenpath: Active.activate() returns same instance; Active.close() -> ClosedCartState")
    void active_activate_returnsSameInstance_and_close_transitionsToClosed_Greenpath() {
        ActiveCartState active = new ActiveCartState();
        assertSame(active, active.activate());
        CartState closed = active.close();
        assertInstanceOf(ClosedCartState.class, closed);
        assertEquals(CartStateType.CLOSED, closed.code());
    }

    @Test
    @DisplayName("Greenpath/Redpath: Ordered.order() returns same; Ordered.create() throws")
    void ordered_order_returnsSameInstance_and_create_throws() {
        OrderedCartState ordered = new OrderedCartState();
        assertSame(ordered, ordered.order());
        assertThrows(IllegalStateException.class, ordered::create);
    }

    @Test
    @DisplayName("Greenpath: Closed.close() returns same instance and code CLOSED")
    void closed_close_returnsSameInstance_Greenpath() {
        ClosedCartState closed = new ClosedCartState();
        assertSame(closed, closed.close());
        assertEquals(CartStateType.CLOSED, closed.code());
    }
}
