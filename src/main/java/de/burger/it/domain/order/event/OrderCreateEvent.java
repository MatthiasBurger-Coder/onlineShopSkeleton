package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;

/**
 * Event emitted when a new order is created.
 */
public record OrderCreateEvent(Order order) {

}