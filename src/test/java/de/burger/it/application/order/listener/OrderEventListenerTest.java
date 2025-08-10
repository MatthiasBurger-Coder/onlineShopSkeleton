package de.burger.it.application.order.listener;

import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.model.OrderDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private ProcessPipeline<OrderCreateEvent> orderCreatePipeline;
    @Mock
    private ProcessPipeline<OrderPayEvent> orderPayPipeline;
    @Mock
    private ProcessPipeline<OrderCancelEvent> orderCancelPipeline;
    @Mock
    private ProcessPipeline<OrderDeliverEvent> orderDeliverPipeline;

    private OrderEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new OrderEventListener(
                orderCreatePipeline,
                orderPayPipeline,
                orderCancelPipeline,
                orderDeliverPipeline
        );
    }

    // GreenPath tests

    @Test
    @DisplayName("GreenPath: handleOrderCreate should delegate to orderCreatePipeline")
    void greenPath_handleOrderCreate_delegatesToPipeline() {
        var event = new OrderCreateEvent(new OrderDefault(UUID.randomUUID()));

        listener.handleOrderCreate(event);

        verify(orderCreatePipeline).execute(event);
        verifyNoMoreInteractions(orderCreatePipeline);
        verifyNoInteractions(orderPayPipeline, orderCancelPipeline, orderDeliverPipeline);
    }

    @Test
    @DisplayName("GreenPath: handleOrderPay should delegate to orderPayPipeline")
    void greenPath_handleOrderPay_delegatesToPipeline() {
        var event = new OrderPayEvent(new OrderDefault(UUID.randomUUID()));

        listener.handleOrderPay(event);

        verify(orderPayPipeline).execute(event);
        verifyNoMoreInteractions(orderPayPipeline);
        verifyNoInteractions(orderCreatePipeline, orderCancelPipeline, orderDeliverPipeline);
    }

    @Test
    @DisplayName("GreenPath: handleOrderCancel should delegate to orderCancelPipeline")
    void greenPath_handleOrderCancel_delegatesToPipeline() {
        var event = new OrderCancelEvent(new OrderDefault(UUID.randomUUID()));

        listener.handleOrderCancel(event);

        verify(orderCancelPipeline).execute(event);
        verifyNoMoreInteractions(orderCancelPipeline);
        verifyNoInteractions(orderCreatePipeline, orderPayPipeline, orderDeliverPipeline);
    }

    @Test
    @DisplayName("GreenPath: handleOrderDeliver should delegate to orderDeliverPipeline")
    void greenPath_handleOrderDeliver_delegatesToPipeline() {
        var event = new OrderDeliverEvent(new OrderDefault(UUID.randomUUID()));

        listener.handleOrderDeliver(event);

        verify(orderDeliverPipeline).execute(event);
        verifyNoMoreInteractions(orderDeliverPipeline);
        verifyNoInteractions(orderCreatePipeline, orderPayPipeline, orderCancelPipeline);
    }

    // RedPath tests

    @Test
    @DisplayName("RedPath: handleOrderCreate should propagate exception thrown by pipeline and not call others")
    void redPath_handleOrderCreate_throwsAndDoesNotInvokeOthers() {
        var event = new OrderCreateEvent(new OrderDefault(UUID.randomUUID()));
        doThrow(new RuntimeException("pipeline failure")).when(orderCreatePipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleOrderCreate(event));

        verify(orderCreatePipeline).execute(event);
        verifyNoInteractions(orderPayPipeline, orderCancelPipeline, orderDeliverPipeline);
    }

    @Test
    @DisplayName("RedPath: handleOrderPay should propagate exception thrown by pipeline and not call others")
    void redPath_handleOrderPay_throwsAndDoesNotInvokeOthers() {
        var event = new OrderPayEvent(new OrderDefault(UUID.randomUUID()));
        doThrow(new RuntimeException("pipeline failure")).when(orderPayPipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleOrderPay(event));

        verify(orderPayPipeline).execute(event);
        verifyNoInteractions(orderCreatePipeline, orderCancelPipeline, orderDeliverPipeline);
    }

    @Test
    @DisplayName("RedPath: handleOrderCancel should propagate exception thrown by pipeline and not call others")
    void redPath_handleOrderCancel_throwsAndDoesNotInvokeOthers() {
        var event = new OrderCancelEvent(new OrderDefault(UUID.randomUUID()));
        doThrow(new RuntimeException("pipeline failure")).when(orderCancelPipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleOrderCancel(event));

        verify(orderCancelPipeline).execute(event);
        verifyNoInteractions(orderCreatePipeline, orderPayPipeline, orderDeliverPipeline);
    }

    @Test
    @DisplayName("RedPath: handleOrderDeliver should propagate exception thrown by pipeline and not call others")
    void redPath_handleOrderDeliver_throwsAndDoesNotInvokeOthers() {
        var event = new OrderDeliverEvent(new OrderDefault(UUID.randomUUID()));
        doThrow(new RuntimeException("pipeline failure")).when(orderDeliverPipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.handleOrderDeliver(event));

        verify(orderDeliverPipeline).execute(event);
        verifyNoInteractions(orderCreatePipeline, orderPayPipeline, orderCancelPipeline);
    }
}
