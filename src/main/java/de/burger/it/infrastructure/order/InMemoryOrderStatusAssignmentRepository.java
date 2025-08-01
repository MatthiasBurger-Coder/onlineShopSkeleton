package de.burger.it.infrastructure.order;

import de.burger.it.domain.order.model.OrderStatusAssignment;
import de.burger.it.infrastructure.cart.port.OrderStatusAssignmentRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderStatusAssignmentRepository implements OrderStatusAssignmentRepository {

    private final Map<UUID, OrderStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public void save(OrderStatusAssignment assignment) {
        store.put(assignment.orderId(), assignment);
    }

    @Override
    public OrderStatusAssignment findBy(UUID customerId) {
        return store.get(customerId);
    }
}
