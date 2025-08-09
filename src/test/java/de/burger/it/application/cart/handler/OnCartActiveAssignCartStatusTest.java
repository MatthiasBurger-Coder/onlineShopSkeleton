package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartActiveEvent;
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
class OnCartActiveAssignCartStatusTest {

    @Mock
    private CartStatusAssignmentPort cartStatusAssignmentPort;

    private OnCartActiveAssignCartStatus handler;

    @BeforeEach
    void setUp() {
        handler = new OnCartActiveAssignCartStatus(cartStatusAssignmentPort);
    }

    @Test
    void execute_shouldAssignActiveState_GreenPath() {
        // Given
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "John Doe", "john@example.com");
        var event = new CartActiveEvent(cart, customer);

        // When
        handler.execute(event);

        // Then
        verify(cartStatusAssignmentPort).assign(cart, CartStateType.ACTIVE);
    }

    @Test
    void execute_whenEventIsNull_shouldThrowException_RedPath() {
        // When / Then
        assertThrows(NullPointerException.class, () -> handler.execute(null));
        verifyNoInteractions(cartStatusAssignmentPort);
    }
}