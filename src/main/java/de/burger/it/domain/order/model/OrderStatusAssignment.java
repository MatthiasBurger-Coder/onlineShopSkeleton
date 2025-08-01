package de.burger.it.domain.order.model;

import de.burger.it.domain.order.state.OrderStateType;

import java.util.UUID;

public record OrderStatusAssignment (UUID orderId, OrderStateType state) {
}
