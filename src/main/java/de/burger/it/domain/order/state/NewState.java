package de.burger.it.domain.order.state;

import org.springframework.stereotype.Component;

@Component
public class NewState implements OrderState {
    public OrderState pay() {
        return new PaidState();
    }

    public OrderState cancel() {
        return new CanceledState();
    }

    @Override
    public OrderState deliver() {
        throw new IllegalStateException("Order not paid");
    }

    public OrderStateType code() {
        return OrderStateType.NEW;
    }
}
