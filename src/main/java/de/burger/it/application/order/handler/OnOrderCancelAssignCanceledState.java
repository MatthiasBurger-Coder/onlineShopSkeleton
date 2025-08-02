package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderCancelAssignCanceledState {

    private final OrderStatusAssignmentPort orderStatusAssignmentPort;

    public OnOrderCancelAssignCanceledState(OrderStatusAssignmentPort orderStatusAssignmentPort) {
        this.orderStatusAssignmentPort = orderStatusAssignmentPort;
    }

    public void execute(OrderCancelEvent event) {
        orderStatusAssignmentPort.assign(event.getOrder(), OrderStateType.CANCELLED);
    }
}