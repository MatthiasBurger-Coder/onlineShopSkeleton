package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.common.event.DomainEvent;

/**
 * Event emitted when an order is delivered.
 */
public class OrderDeliverEvent extends DomainEvent {

    private final Order order;

    public OrderDeliverEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}