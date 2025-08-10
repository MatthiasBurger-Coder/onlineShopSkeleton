package de.burger.it.domain.order.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStateTest {

    @Nested
    @DisplayName("Greenpath: valid order state transitions")
    class Greenpath {
        @Test
        void new_to_paid_to_delivered() {
            // Given
            OrderState state = new NewState();

            // When
            OrderState paid = state.pay();
            OrderState delivered = paid.deliver();

            // Then
            assertInstanceOf(PaidState.class, paid);
            assertEquals(OrderStateType.PAID, paid.code());

            assertInstanceOf(DeliveredState.class, delivered);
            assertEquals(OrderStateType.DELIVERED, delivered.code());
        }

        @Test
        void new_to_canceled() {
            OrderState state = new NewState();
            OrderState canceled = state.cancel();
            assertInstanceOf(CanceledState.class, canceled);
            assertEquals(OrderStateType.CANCELLED, canceled.code());
        }
    }

    @Nested
    @DisplayName("Redpath: invalid order state transitions throw")
    class Redpath {
        @Test
        void new_deliver_shouldThrow() {
            OrderState state = new NewState();
            assertThrows(IllegalStateException.class, state::deliver);
        }

        @Test
        void paid_cancel_shouldThrow() {
            OrderState state = new PaidState();
            assertThrows(IllegalStateException.class, state::cancel);
        }

        @Test
        void delivered_pay_or_cancel_shouldThrow() {
            OrderState state = new DeliveredState();
            assertThrows(IllegalStateException.class, state::pay);
            assertThrows(IllegalStateException.class, state::cancel);
        }
    }
}
