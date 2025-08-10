package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCancelEvent;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OnOrderCancelAssignCanceledStateTest {

    @Mock
    private OrderStatusAssignmentPort orderStatusAssignment;

    private OnOrderCancelAssignCanceledState handler;

    @BeforeEach
    void setUp() {
        handler = new OnOrderCancelAssignCanceledState(orderStatusAssignment);
    }

    // GreenPath
    @Test
    void execute_shouldAssignCanceledState_GreenPath() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderCancelEvent(order);

        handler.execute(event);

        verify(orderStatusAssignment).assign(order, OrderStateType.CANCELLED);
    }

    // RedPath
    @Test
    void execute_whenAssignmentThrows_shouldPropagate_RedPath() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderCancelEvent(order);

        doThrow(new RuntimeException("assign failed"))
                .when(orderStatusAssignment).assign(order, OrderStateType.CANCELLED);

        assertThrows(RuntimeException.class, () -> handler.execute(event));
        verify(orderStatusAssignment).assign(order, OrderStateType.CANCELLED);
    }
}
