package de.burger.it.domain.customer.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerStateTest {

    @Nested
    @DisplayName("Greenpath: valid customer state transitions")
    class Greenpath {
        @Test
        void created_to_activated_to_suspended_and_back_to_activated() {
            // Given
            CustomerState state = new CreatedState();

            // When
            CustomerState created = state.create();
            CustomerState activated = created.active();
            CustomerState suspended = activated.suspend();
            CustomerState activatedAgain = suspended.active();

            // Then
            assertInstanceOf(CreatedState.class, created);
            assertEquals(CustomerStateType.CREATE, created.code());

            assertInstanceOf(ActivatedState.class, activated);
            assertEquals(CustomerStateType.ACTIVE, activated.code());

            assertInstanceOf(SuspendedState.class, suspended);
            assertEquals(CustomerStateType.SUSPENDED, suspended.code());

            assertInstanceOf(ActivatedState.class, activatedAgain);
            assertEquals(CustomerStateType.ACTIVE, activatedAgain.code());
        }
    }

    @Nested
    @DisplayName("Redpath: invalid customer state transitions throw")
    class Redpath {
        @Test
        void created_suspend_shouldThrow() {
            CustomerState state = new CreatedState();
            assertThrows(IllegalStateException.class, state::suspend);
        }

        @Test
        void activated_create_shouldThrow() {
            CustomerState state = new ActivatedState();
            assertThrows(IllegalStateException.class, state::create);
        }

        @Test
        void suspended_create_shouldThrow() {
            CustomerState state = new SuspendedState();
            assertThrows(IllegalStateException.class, state::create);
        }
    }
}
