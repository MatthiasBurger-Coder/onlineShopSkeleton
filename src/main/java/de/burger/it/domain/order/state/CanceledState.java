package de.burger.it.domain.order.state;

import org.springframework.stereotype.Component;

@Component
public class CanceledState implements OrderState {
    public OrderState pay() {
        throw new IllegalStateException("Already canceled");
    }

    public OrderState cancel() {
        return this;
    }

    public OrderState deliver() {throw new IllegalStateException("Already canceled"); }

    public OrderStateType code() {
        return OrderStateType.CANCELLED;
    }
}
