package de.burger.it.application.cart.process;

import de.burger.it.application.cart.handler.OnCartActiveAssignCartStatus;
import de.burger.it.application.cart.handler.OnCartCloseAssignCartStatus;
import de.burger.it.application.cart.handler.OnCartCreateAssignCartStatus;
import de.burger.it.application.cart.handler.OnCartCreateAssignCustomer;
import de.burger.it.application.cart.handler.OnCartCreateSaveRepository;
import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartProcessConfigTest {

    @Mock
    private OnCartCreateAssignCartStatus onCartCreateAssignCartStatus;
    @Mock
    private OnCartCreateAssignCustomer onCartCreateAssignCustomer;
    @Mock
    private OnCartCreateSaveRepository onCartCreateSaveRepository;

    @Mock
    private OnCartActiveAssignCartStatus onCartActiveAssignCartStatus;
    @Mock
    private OnCartCloseAssignCartStatus onCartCloseAssignCartStatus;

    private CartProcessConfig config;

    @BeforeEach
    void setUp() {
        config = new CartProcessConfig();
    }

    @Test
    void cartCreateProcessPipeline_shouldExecuteHandlersInOrder_GreenPath() {
        // Given
        ProcessPipeline<CartCreateEvent> pipeline = config.cartCreateProcessPipeline(
                onCartCreateAssignCartStatus,
                onCartCreateAssignCustomer,
                onCartCreateSaveRepository
        );
        var event = new CartCreateEvent(new CartDefault(UUID.randomUUID()),
                new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com"));

        // When
        CartCreateEvent result = pipeline.execute(event);

        // Then
        InOrder inOrder = Mockito.inOrder(onCartCreateAssignCartStatus, onCartCreateAssignCustomer, onCartCreateSaveRepository);
        inOrder.verify(onCartCreateAssignCartStatus).execute(event);
        inOrder.verify(onCartCreateAssignCustomer).execute(event);
        inOrder.verify(onCartCreateSaveRepository).execute(event);
        // Pipeline should return the same event instance (consumer steps don't transform)
        assertSame(event, result);
    }

    @Test
    void cartCreateProcessPipeline_whenFirstHandlerThrows_shouldShortCircuit_RedPath() {
        // Given
        ProcessPipeline<CartCreateEvent> pipeline = config.cartCreateProcessPipeline(
                onCartCreateAssignCartStatus,
                onCartCreateAssignCustomer,
                onCartCreateSaveRepository
        );
        var event = new CartCreateEvent(new CartDefault(UUID.randomUUID()),
                new CustomerDefault(UUID.randomUUID(), "Erika Musterfrau", "erika@example.com"));

        doThrow(new IllegalStateException("boom")).when(onCartCreateAssignCartStatus).execute(event);

        // When / Then
        assertThrows(IllegalStateException.class, () -> pipeline.execute(event));
        verify(onCartCreateAssignCartStatus).execute(event);
        verifyNoInteractions(onCartCreateAssignCustomer);
        verifyNoInteractions(onCartCreateSaveRepository);
    }

    @Test
    void cartCreateProcessPipeline_whenSecondHandlerThrows_shouldNotCallThird_RedPath() {
        // Given
        ProcessPipeline<CartCreateEvent> pipeline = config.cartCreateProcessPipeline(
                onCartCreateAssignCartStatus,
                onCartCreateAssignCustomer,
                onCartCreateSaveRepository
        );
        var event = new CartCreateEvent(new CartDefault(UUID.randomUUID()),
                new CustomerDefault(UUID.randomUUID(), "Erik Example", "erik@example.com"));

        // First succeeds, second throws
        doNothing().when(onCartCreateAssignCartStatus).execute(event);
        doThrow(new RuntimeException("fail at customer")).when(onCartCreateAssignCustomer).execute(event);

        // When / Then
        assertThrows(RuntimeException.class, () -> pipeline.execute(event));
        verify(onCartCreateAssignCartStatus).execute(event);
        verify(onCartCreateAssignCustomer).execute(event);
        verifyNoInteractions(onCartCreateSaveRepository);
    }

    @Test
    void cartActiveProcessPipeline_shouldExecuteSingleHandler_GreenPath() {
        // Given
        ProcessPipeline<CartActiveEvent> pipeline = config.cartActiveProcessPipeline(onCartActiveAssignCartStatus);
        var event = new CartActiveEvent(new CartDefault(UUID.randomUUID()),
                new CustomerDefault(UUID.randomUUID(), "John Doe", "john@example.com"));

        // When
        CartActiveEvent result = pipeline.execute(event);

        // Then
        verify(onCartActiveAssignCartStatus).execute(event);
        assertSame(event, result);
    }

    @Test
    void cartCloseProcessPipeline_shouldExecuteSingleHandler_GreenPath() {
        // Given
        ProcessPipeline<CartCloseEvent> pipeline = config.cartCloseProcessPipeline(onCartCloseAssignCartStatus);
        var event = new CartCloseEvent(new CartDefault(UUID.randomUUID()),
                new CustomerDefault(UUID.randomUUID(), "Jane Doe", "jane@example.com"));

        // When
        CartCloseEvent result = pipeline.execute(event);

        // Then
        verify(onCartCloseAssignCartStatus).execute(event);
        assertSame(event, result);
    }

    @Test
    void cartActiveProcessPipeline_whenEventIsNull_shouldPropagateException_RedPath() {
        // Given
        ProcessPipeline<CartActiveEvent> pipeline = config.cartActiveProcessPipeline(onCartActiveAssignCartStatus);
        doThrow(new NullPointerException("event is null")).when(onCartActiveAssignCartStatus).execute(null);

        // When / Then
        assertThrows(NullPointerException.class, () -> pipeline.execute(null));
        verify(onCartActiveAssignCartStatus).execute(null);
    }

    @Test
    void cartCloseProcessPipeline_whenEventIsNull_shouldPropagateException_RedPath() {
        // Given
        ProcessPipeline<CartCloseEvent> pipeline = config.cartCloseProcessPipeline(onCartCloseAssignCartStatus);
        doThrow(new NullPointerException("event is null")).when(onCartCloseAssignCartStatus).execute(null);

        // When / Then
        assertThrows(NullPointerException.class, () -> pipeline.execute(null));
        verify(onCartCloseAssignCartStatus).execute(null);
    }
}
