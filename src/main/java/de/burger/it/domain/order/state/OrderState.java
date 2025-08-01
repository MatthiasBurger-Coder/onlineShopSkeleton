package de.burger.it.domain.order.state;

public interface OrderState {
    OrderState pay();

    OrderState cancel();

    OrderState deliver();

    OrderStateType code();
}
