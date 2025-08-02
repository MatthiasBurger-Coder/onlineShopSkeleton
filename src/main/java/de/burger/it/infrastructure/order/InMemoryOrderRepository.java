package de.burger.it.infrastructure.order;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderLike;
import de.burger.it.domain.order.model.NullOrder;
import de.burger.it.domain.order.port.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository implements OrderRepositoryPort {

    private final Map<UUID, Order> store = new ConcurrentHashMap<>();

    @Override
    public OrderLike findById(UUID orderId) {
        Order order = store.get(orderId);
        return order != null ? order : NullOrder.getInstance();
    }

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }
}
