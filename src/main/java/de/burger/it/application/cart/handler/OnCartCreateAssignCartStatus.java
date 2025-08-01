package de.burger.it.application.cart.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCartCreateAssignCartStatus implements StateEventHandler<CartStateType, CartCreateEvent> {

    private final CartStatusAssignmentPort cartStatusAssignmentAdapter;

    public OnCartCreateAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentAdapter) {
        this.cartStatusAssignmentAdapter = cartStatusAssignmentAdapter;
    }

    @Override
    public Collection<CartStateType> supportedStates() {
        return List.of(CartStateType.CREATED);
    }

    @Override
    public Class<CartCreateEvent> supportedEvent() {
        return CartCreateEvent.class;
    }

    @Override
    public void execute(CartCreateEvent event) {
        cartStatusAssignmentAdapter.assign(event.cart(), CartStateType.CREATED);
    }
}
