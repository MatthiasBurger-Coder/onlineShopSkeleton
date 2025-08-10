package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderPayEvent;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class OnOrderPayAssignPaidStateTest {

    @Mock
    private OrderStatusAssignmentPort orderStatusAssignment;

    private OnOrderPayAssignPaidState handler;

    @BeforeEach
    void setUp() {
        handler = new OnOrderPayAssignPaidState(orderStatusAssignment);
    }

    // GreenPath
    @Test
    void execute_shouldAssignPaidState_GreenPath() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderPayEvent(order);

        handler.execute(event);

        verify(orderStatusAssignment).assign(order, OrderStateType.PAID);
        verifyNoMoreInteractions(orderStatusAssignment);
    }

    // RedPath
    @Test
    void execute_whenAssignmentThrows_shouldPropagate_RedPath() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderPayEvent(order);

        doThrow(new RuntimeException("assign failed"))
                .when(orderStatusAssignment).assign(order, OrderStateType.PAID);

        assertThrows(RuntimeException.class, () -> handler.execute(event));
        verify(orderStatusAssignment).assign(order, OrderStateType.PAID);
    }
}
