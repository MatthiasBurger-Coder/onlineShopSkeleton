package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OnCartCloseAssignCartStatusTest {

    @Mock
    private CartStatusAssignmentPort cartStatusAssignmentPort;

    private OnCartCloseAssignCartStatus handler;

    @BeforeEach
    void setUp() {
        handler = new OnCartCloseAssignCartStatus(cartStatusAssignmentPort);
    }

    @Test
    void execute_shouldAssignClosedState_GreenPath() {
        // Given
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Jane Doe", "jane@example.com");
        var event = new CartCloseEvent(cart, customer);

        // When
        handler.execute(event);

        // Then
        verify(cartStatusAssignmentPort).assign(cart, CartStateType.CLOSED);
    }

    @Test
    void execute_whenEventIsNull_shouldThrowException_RedPath() {
        // When / Then
        assertThrows(NullPointerException.class, () -> handler.execute(null));
        verifyNoInteractions(cartStatusAssignmentPort);
    }
}
