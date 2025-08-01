package de.burger.it.domain.cart.state;

import lombok.ToString;

@ToString
public class ClosedCartState implements CartState {

    @Override
    public CartState create() {
        throw new IllegalStateException("Cart is already created");
    }

    @Override
    public CartState activate() {
        throw new IllegalStateException("Cart can't be activated after closing");
    }

    @Override
    public CartState order() {
        throw new IllegalStateException("Cart can't be set to order state after closing");
    }

    @Override
    public CartState close() {
        return this;
    }

    @Override
    public CartStateType code() {
        return CartStateType.CLOSED;
    }
}
