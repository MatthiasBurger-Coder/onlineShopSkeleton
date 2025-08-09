package de.burger.it.domain.order.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the new state of an order.
 * This is the initial state when an order is first created.
 */
@ToString
@NoArgsConstructor
public class NewState implements OrderState {
    
    @Override
    public OrderState pay() {
        return new PaidState();
    }

    @Override
    public OrderState cancel() {
        return new CanceledState();
    }

    @Override
    public OrderState deliver() {
        throw new IllegalStateException("OrderDefault not paid");
    }

    @Override
    public OrderStateType code() {
        return OrderStateType.NEW;
    }
}
