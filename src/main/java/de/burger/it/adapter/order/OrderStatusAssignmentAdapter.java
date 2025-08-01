package de.burger.it.adapter.order;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderStatusAssignment;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.infrastructure.cart.port.OrderStatusAssignmentRepository;
import de.burger.it.domain.order.state.OrderStateType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderStatusAssignmentAdapter implements OrderStatusAssignmentPort {

    private final OrderStatusAssignmentRepository repository;

    public OrderStatusAssignmentAdapter(OrderStatusAssignmentRepository repository) {
        this.repository = repository;
    }


    @Override
    public OrderStateType findBy(UUID customerId) {
        return repository.findBy(customerId).state();
    }

    @Override
    public void assign(Order order, OrderStateType newState) {
        var assignment = new OrderStatusAssignment(
                order.id(),
                newState
        );
        repository.save(assignment);
    }
}
