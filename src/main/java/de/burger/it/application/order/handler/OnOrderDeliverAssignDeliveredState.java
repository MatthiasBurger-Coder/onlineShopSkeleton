package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderDeliverAssignDeliveredState {

    private final OrderStatusAssignmentPort orderStatusAssignmentPort;

    public OnOrderDeliverAssignDeliveredState(OrderStatusAssignmentPort orderStatusAssignmentPort) {
        this.orderStatusAssignmentPort = orderStatusAssignmentPort;
    }

    public void execute(OrderDeliverEvent event) {
        orderStatusAssignmentPort.assign(event.getOrder(), OrderStateType.DELIVERED);
    }
}