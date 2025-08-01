package de.burger.it.infrastructure.order.adapter;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderStatusAssignment;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStatusAssignmentAdapter implements OrderStatusAssignmentPort {

    private final Map<UUID, OrderStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public OrderStateType findBy(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return store.get(orderId).state();
    }

    @Override
    public void assign(Order order, OrderStateType newState) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (newState == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        var assignment = new OrderStatusAssignment(
                order.id(),
                newState
        );
        store.put(assignment.orderId(), assignment);
    }
}