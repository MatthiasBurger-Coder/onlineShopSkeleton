package de.burger.it.domain.cart.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartStateTest {

    @Nested
    @DisplayName("Greenpath: valid cart state transitions")
    class Greenpath {
        @Test
        void created_to_active_to_ordered_to_closed() {
            // Given
            CartState state = new CreatedCartState();

            // When
            CartState created = state.create();
            CartState active = created.activate();
            CartState ordered = active.order();
            CartState closed = ordered.close();

            // Then
            assertInstanceOf(CreatedCartState.class, created);
            assertEquals(CartStateType.CREATED, created.code());

            assertInstanceOf(ActiveCartState.class, active);
            assertEquals(CartStateType.ACTIVE, active.code());

            assertInstanceOf(OrderedCartState.class, ordered);
            assertEquals(CartStateType.ORDERED, ordered.code());

            assertInstanceOf(ClosedCartState.class, closed);
            assertEquals(CartStateType.CLOSED, closed.code());
        }

        @Test
        void null_state_all_transitions_return_self_and_notDefined_returns_self() {
            // Given
            CartState state = new NullCartState();

            // When
            CartState created = state.create();
            CartState active = state.activate();
            CartState ordered = state.order();
            CartState closed = state.close();
            CartState undefined = state.notDefined();

            // Then
            assertSame(state, created);
            assertSame(state, active);
            assertSame(state, ordered);
            assertSame(state, closed);
            assertSame(state, undefined);
            assertEquals(CartStateType.CREATED, state.code(), "Null state defaults to CREATED code");
        }
    }

    @Nested
    @DisplayName("Redpath: invalid cart state transitions throw")
    class Redpath {
        @Test
        void created_order_shouldThrow() {
            CartState state = new CreatedCartState();
            assertThrows(IllegalStateException.class, state::order);
        }

        @Test
        void active_create_shouldThrow() {
            CartState state = new ActiveCartState();
            assertThrows(IllegalStateException.class, state::create);
        }

        @Test
        void ordered_activate_shouldThrow() {
            CartState state = new OrderedCartState();
            assertThrows(IllegalStateException.class, state::activate);
        }

        @Test
        void closed_activate_order_create_shouldThrow() {
            CartState state = new ClosedCartState();
            assertThrows(IllegalStateException.class, state::activate);
            assertThrows(IllegalStateException.class, state::order);
            assertThrows(IllegalStateException.class, state::create);
        }

        @Test
        void active_notDefined_shouldThrow() {
            CartState state = new ActiveCartState();
            assertThrows(IllegalStateException.class, state::notDefined);
        }

        @Test
        void created_notDefined_shouldThrow() {
            CartState state = new CreatedCartState();
            assertThrows(IllegalStateException.class, state::notDefined);
        }

        @Test
        void ordered_notDefined_shouldThrow() {
            CartState state = new OrderedCartState();
            assertThrows(IllegalStateException.class, state::notDefined);
        }

        @Test
        void closed_notDefined_shouldThrow() {
            CartState state = new ClosedCartState();
            assertThrows(IllegalStateException.class, state::notDefined);
        }
    }
}
