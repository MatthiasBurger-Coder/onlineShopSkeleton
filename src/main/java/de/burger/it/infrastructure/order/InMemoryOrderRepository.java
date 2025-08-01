package de.burger.it.infrastructure.order;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.port.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> store = new HashMap<>();

    @Override
    public Optional<Order> findById(UUID orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }
}
