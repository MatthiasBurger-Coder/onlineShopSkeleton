package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderDeliverAssignDeliveredState {

    private final OrderStatusAssignmentPort orderStatusAssignment;

    public OnOrderDeliverAssignDeliveredState(OrderStatusAssignmentPort orderStatusAssignment) {
        this.orderStatusAssignment = orderStatusAssignment;
    }

    public void execute(OrderDeliverEvent event) {
        orderStatusAssignment.assign(event.order(), OrderStateType.DELIVERED);
    }
}