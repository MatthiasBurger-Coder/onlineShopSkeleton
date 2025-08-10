package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderCancelAssignCanceledState {

    private final OrderStatusAssignmentPort orderStatusAssignment;

    public OnOrderCancelAssignCanceledState(OrderStatusAssignmentPort orderStatusAssignment) {
        this.orderStatusAssignment = orderStatusAssignment;
    }

    public void execute(OrderCancelEvent event) {
        orderStatusAssignment.assign(event.order(), OrderStateType.CANCELLED);
    }
}