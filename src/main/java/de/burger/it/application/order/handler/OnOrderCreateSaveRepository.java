package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.port.OrderRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class OnOrderCreateSaveRepository {

    private final OrderRepositoryPort orderRepository;

    public OnOrderCreateSaveRepository(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void execute(OrderCreateEvent event) {
        orderRepository.save(event.getOrder());
    }
}