package de.burger.it.domain.order.port;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.state.OrderStateType;

import java.util.UUID;

public interface OrderStatusAssignmentPort {

    OrderStateType findBy(UUID orderId);

    void assign(Order order, OrderStateType newState);
}
