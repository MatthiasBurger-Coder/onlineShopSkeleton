package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderPayAssignPaidState {

    private final OrderStatusAssignmentPort orderStatusAssignmentPort;

    public OnOrderPayAssignPaidState(OrderStatusAssignmentPort orderStatusAssignmentPort) {
        this.orderStatusAssignmentPort = orderStatusAssignmentPort;
    }

    public void execute(OrderPayEvent event) {
        orderStatusAssignmentPort.assign(event.order(), OrderStateType.PAID);
    }
}