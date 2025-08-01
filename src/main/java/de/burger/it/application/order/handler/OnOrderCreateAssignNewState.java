package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

@Component
public class OnOrderCreateAssignNewState {

    private final OrderStatusAssignmentPort orderStatusAssignmentPort;

    public OnOrderCreateAssignNewState(OrderStatusAssignmentPort orderStatusAssignmentPort) {
        this.orderStatusAssignmentPort = orderStatusAssignmentPort;
    }

    public void execute(OrderCreateEvent event) {
        orderStatusAssignmentPort.assign(event.order(), OrderStateType.NEW);
    }
}