package de.burger.it.domain.order.state;

public class PaidState implements OrderState {
    public OrderState pay() {
        return this;
    }

    public OrderState cancel() {
        throw new IllegalStateException("Can't cancel paid order");
    }

    public OrderState deliver() { return new DeliveredState(); }

    public OrderStateType code() {
        return OrderStateType.PAID;
    }
}
