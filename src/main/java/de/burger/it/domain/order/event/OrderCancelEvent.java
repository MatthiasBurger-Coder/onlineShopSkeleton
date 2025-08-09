package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;

/**
 * Event emitted when an order is cancelled.
 */
public record OrderCancelEvent(Order order) {

}