package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderPayAssignPaidState {

    private final OrderStatusAssignmentPort orderStatusAssignment;

    public OnOrderPayAssignPaidState(OrderStatusAssignmentPort orderStatusAssignment) {
        this.orderStatusAssignment = orderStatusAssignment;
    }

    public void execute(OrderPayEvent event) {
        orderStatusAssignment.assign(event.order(), OrderStateType.PAID);
    }
}