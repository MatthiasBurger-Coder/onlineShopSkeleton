package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
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
class OnCartCreateAssignCustomerTest {

    @Mock
    private CartCustomerAssignmentPort cartCustomerAssignmentPort;

    private OnCartCreateAssignCustomer handler;

    @BeforeEach
    void setUp() {
        handler = new OnCartCreateAssignCustomer(cartCustomerAssignmentPort);
    }

    @Test
    void execute_shouldAssignCustomer_GreenPath() {
        // Given
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com");
        var event = new CartCreateEvent(cart, customer);

        // When
        handler.execute(event);

        // Then
        verify(cartCustomerAssignmentPort).assign(cart, customer);
    }

    @Test
    void execute_whenEventIsNull_shouldThrowException_RedPath() {
        // When / Then
        assertThrows(NullPointerException.class, () -> handler.execute(null));
        verifyNoInteractions(cartCustomerAssignmentPort);
    }
}
