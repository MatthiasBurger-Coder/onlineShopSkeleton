package de.burger.it.domain.order.state;

import org.springframework.stereotype.Component;

@Component
public class PaidState implements OrderState {
    public OrderState pay() {
        return this;
    }

    public OrderState cancel() {
        throw new IllegalStateException("Can't cancel paid order");
    }

    public OrderState deliver() { return new DelieveredState(); }

    public OrderStateType code() {
        return OrderStateType.PAID;
    }
}
