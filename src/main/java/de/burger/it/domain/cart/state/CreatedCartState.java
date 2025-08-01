package de.burger.it.domain.cart.state;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class CreatedCartState implements CartState {

    @Override
    public CartState create() {
        return this;
    }

    @Override
    public CartState activate() {
        return new ActiveCartState();
    }

    @Override
    public CartState order() {
        throw new IllegalStateException("Cart must be active before ordering");
    }

    @Override
    public CartState close() {
        return new ClosedCartState();
    }

    @Override
    public CartStateType code() {
        return CartStateType.CREATED;
    }
}
