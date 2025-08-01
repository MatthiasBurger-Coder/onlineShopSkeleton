package de.burger.it.domain.order.state;

public class DelieveredState implements OrderState {
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
        return  OrderStateType.DELIVERED;
    }
}
