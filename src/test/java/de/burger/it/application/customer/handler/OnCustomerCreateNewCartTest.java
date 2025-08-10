package de.burger.it.application.customer.handler;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
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
class OnCustomerCreateNewCartTest {

    @Mock
    private CartService cartService;

    private OnCustomerCreateNewCart handler;

    @BeforeEach
    void setUp() {
        handler = new OnCustomerCreateNewCart(cartService);
    }

    @Test
    void execute_shouldCreateNewCart_GreenPath() {
        // Given
        var customer = new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com");
        var event = new CustomerCreateEvent(customer);

        // When
        handler.execute(event);

        // Then
        verify(cartService).create(customer);
    }

    @Test
    void execute_whenEventIsNull_shouldThrowException_RedPath() {
        // When / Then
        assertThrows(NullPointerException.class, () -> handler.execute(null));
        verifyNoInteractions(cartService);
    }
}
