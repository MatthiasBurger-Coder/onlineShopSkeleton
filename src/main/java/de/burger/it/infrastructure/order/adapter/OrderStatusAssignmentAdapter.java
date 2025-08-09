package de.burger.it.infrastructure.order.adapter;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderStatusAssignment;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStatusAssignmentAdapter implements OrderStatusAssignmentPort {

    private final Map<UUID, OrderStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public OrderStateType findBy(UUID orderId) {
        var id = Optional.ofNullable(orderId)
                .orElseThrow(() -> new IllegalArgumentException("OrderDefault ID cannot be null"));
        return Optional.ofNullable(store.get(id))
                .map(OrderStatusAssignment::state)
                .orElse(null);
    }

    @Override
    public void assign(Order order, OrderStateType newState) {
        var nonNullOrder = Optional.ofNullable(order)
                .orElseThrow(() -> new IllegalArgumentException("OrderDefault cannot be null"));
        var state = Optional.ofNullable(newState)
                .orElseThrow(() -> new IllegalArgumentException("State cannot be null"));
        var assignment = new OrderStatusAssignment(
                nonNullOrder.id(),
                state
        );
        store.put(assignment.orderId(), assignment);
    }
}