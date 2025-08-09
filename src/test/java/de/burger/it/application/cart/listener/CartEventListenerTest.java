package de.burger.it.application.cart.listener;

import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartEventListenerTest {

    @Mock
    private ProcessPipeline<CartCreateEvent> cartCreatePipeline;
    @Mock
    private ProcessPipeline<CartActiveEvent> cartActivePipeline;
    @Mock
    private ProcessPipeline<CartCloseEvent> cartClosePipeline;

    private CartEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new CartEventListener(cartCreatePipeline, cartActivePipeline, cartClosePipeline);
    }

    // Green path tests
    @Test
    void handleCartCreated_shouldDelegateToCreatePipeline_GreenPath() {
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "John Doe", "john@example.com");
        var event = new CartCreateEvent(cart, customer);

        listener.handleCartCreated(event);

        verify(cartCreatePipeline).execute(event);
        verifyNoInteractions(cartActivePipeline, cartClosePipeline);
    }

    @Test
    void handleCartActivate_shouldDelegateToActivePipeline_GreenPath() {
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Jane Doe", "jane@example.com");
        var event = new CartActiveEvent(cart, customer);

        listener.handleCartActivate(event);

        verify(cartActivePipeline).execute(event);
        verifyNoInteractions(cartCreatePipeline, cartClosePipeline);
    }

    @Test
    void handleCartClose_shouldDelegateToClosePipeline_GreenPath() {
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com");
        var event = new CartCloseEvent(cart, customer);

        listener.handleCartClose(event);

        verify(cartClosePipeline).execute(event);
        verifyNoInteractions(cartCreatePipeline, cartActivePipeline);
    }

    // Red path tests
    @Test
    void handleCartCreated_whenPipelineThrows_shouldPropagate_RedPath() {
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "John Error", "error1@example.com");
        var event = new CartCreateEvent(cart, customer);

        doThrow(new RuntimeException("create failed")).when(cartCreatePipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleCartCreated(event));
        verify(cartCreatePipeline).execute(event);
        verifyNoInteractions(cartActivePipeline, cartClosePipeline);
    }

    @Test
    void handleCartActivate_whenPipelineThrows_shouldPropagate_RedPath() {
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Jane Error", "error2@example.com");
        var event = new CartActiveEvent(cart, customer);

        doThrow(new RuntimeException("activate failed")).when(cartActivePipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleCartActivate(event));
        verify(cartActivePipeline).execute(event);
        verifyNoInteractions(cartCreatePipeline, cartClosePipeline);
    }

    @Test
    void handleCartClose_whenPipelineThrows_shouldPropagate_RedPath() {
        var cart = new CartDefault(UUID.randomUUID());
        var customer = new CustomerDefault(UUID.randomUUID(), "Max Error", "error3@example.com");
        var event = new CartCloseEvent(cart, customer);

        doThrow(new RuntimeException("close failed")).when(cartClosePipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleCartClose(event));
        verify(cartClosePipeline).execute(event);
        verifyNoInteractions(cartCreatePipeline, cartActivePipeline);
    }
}
