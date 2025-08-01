package de.burger.it.domain.order.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the canceled state of an order.
 * This state indicates that the order has been canceled and cannot be processed further.
 */
@ToString
@NoArgsConstructor
public class CanceledState implements OrderState {
    
    @Override
    public OrderState pay() {
        throw new IllegalStateException("Already canceled");
    }

    @Override
    public OrderState cancel() {
        return this;
    }

    @Override
    public OrderState deliver() {
        throw new IllegalStateException("Already canceled");
    }

    @Override
    public OrderStateType code() {
        return OrderStateType.CANCELLED;
    }
}
