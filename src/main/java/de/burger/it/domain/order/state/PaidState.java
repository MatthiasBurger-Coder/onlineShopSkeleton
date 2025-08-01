package de.burger.it.domain.order.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the paid state of an order.
 * This state indicates that the order has been paid for but not yet delivered.
 */
@ToString
@NoArgsConstructor
public class PaidState implements OrderState {
    
    @Override
    public OrderState pay() {
        return this;
    }

    @Override
    public OrderState cancel() {
        throw new IllegalStateException("Can't cancel paid order");
    }

    @Override
    public OrderState deliver() {
        return new DeliveredState();
    }

    @Override
    public OrderStateType code() {
        return OrderStateType.PAID;
    }
}
