package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCreateEvent;
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
class OnCartCreateAssignCartStatusTest {

    @Mock
    private CartStatusAssignmentPort cartStatusAssignmentPort;

    private OnCartCreateAssignCartStatus handler;

    @BeforeEach
    void setUp() {
        handler = new OnCartCreateAssignCartStatus(cartStatusAssignmentPort);
    }

    @Test
    void execute_shouldAssignCreatedState_GreenPath() {
        // Given
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com");
        var event = new CartCreateEvent(cart, customer);

        // When
        handler.execute(event);

        // Then
        verify(cartStatusAssignmentPort).assign(cart, CartStateType.CREATED);
    }

    @Test
    void execute_whenEventIsNull_shouldThrowException_RedPath() {
        // When / Then
        assertThrows(NullPointerException.class, () -> handler.execute(null));
        verifyNoInteractions(cartStatusAssignmentPort);
    }
}
