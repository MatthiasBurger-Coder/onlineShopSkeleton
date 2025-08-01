package de.burger.it.domain.order.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the delivered state of an order.
 * This state indicates that the order has been delivered to the customer.
 */
@ToString
@NoArgsConstructor
public class DeliveredState implements OrderState {
    
    @Override
    public OrderState pay() {
        throw new IllegalStateException("Already paid");
    }

    @Override
    public OrderState cancel() {
        throw new IllegalStateException("Can't be cancelled");
    }

    @Override
    public OrderState deliver() {
        return this;
    }

    @Override
    public OrderStateType code() {
        return OrderStateType.DELIVERED;
    }
}
