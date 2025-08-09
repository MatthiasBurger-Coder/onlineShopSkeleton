package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when an order is delivered.
 */
@Getter
public class OrderDeliverEvent extends DomainEvent {

    private final Order order;

    public OrderDeliverEvent(Order order) {
        this.order = order;
    }

}