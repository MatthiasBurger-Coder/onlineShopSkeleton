package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when a new order is created.
 */
@Getter
public class OrderCreateEvent extends DomainEvent {

    private final Order order;

    public OrderCreateEvent(Order order) {
        this.order = order;
    }

}