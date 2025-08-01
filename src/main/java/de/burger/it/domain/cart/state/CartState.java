package de.burger.it.domain.cart.state;

public interface CartState {

    CartState create();

    CartState activate();

    CartState order();

    CartState close();

    CartStateType code();
}
