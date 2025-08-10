package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OnOrderCreateAssignNewStateTest {

    @Mock
    private OrderStatusAssignmentPort orderStatusAssignmentPort;

    private OnOrderCreateAssignNewState handler;

    @BeforeEach
    void setUp() {
        handler = new OnOrderCreateAssignNewState(orderStatusAssignmentPort);
    }

    // Greenpath: Should assign NEW state when a valid order create event is executed
    @Test
    void execute_shouldAssignNewState() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderCreateEvent(order);

        handler.execute(event);

        verify(orderStatusAssignmentPort).assign(order, OrderStateType.NEW);
        verifyNoMoreInteractions(orderStatusAssignmentPort);
    }

    // Redpath: If the assignment port throws, the exception should propagate
    @Test
    void execute_whenAssignmentThrows_shouldPropagateException() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderCreateEvent(order);

        doThrow(new RuntimeException("assignment failed"))
                .when(orderStatusAssignmentPort)
                .assign(order, OrderStateType.NEW);

        assertThrows(RuntimeException.class, () -> handler.execute(event));
        verify(orderStatusAssignmentPort).assign(order, OrderStateType.NEW);
    }
}
