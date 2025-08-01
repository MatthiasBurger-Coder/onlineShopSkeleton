package de.burger.it.domain.cart.state;

import lombok.ToString;

@ToString
public class OrderedCartState implements CartState {

    @Override
    public CartState create() {
        throw new IllegalStateException("Cart is already created");
    }

    @Override
    public CartState activate() {
        throw new IllegalStateException("Cannot activate ordered cart");
    }

    @Override
    public CartState order() {
        return this;
    }

    @Override
    public CartState close() {
        return new ClosedCartState();
    }

    @Override
    public CartStateType code() {
        return CartStateType.ORDERED;
    }
}
