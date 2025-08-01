package de.burger.it.application.cart.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCartActiveAssignCartStatus implements StateEventHandler<CartStateType, CartActiveEvent> {

    private final CartStatusAssignmentPort cartStatusAssignmentAdapter;

    public OnCartActiveAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentAdapter) {
        this.cartStatusAssignmentAdapter = cartStatusAssignmentAdapter;
    }

    @Override
    public Collection<CartStateType> supportedStates() {

        return List.of(CartStateType.ACTIVE,CartStateType.CREATED,CartStateType.CLOSED);
    }

    @Override
    public Class<CartActiveEvent> supportedEvent() {
        return CartActiveEvent.class;
    }

    @Override
    public void execute(CartActiveEvent event) {
        cartStatusAssignmentAdapter.assign(event.cart(), CartStateType.ACTIVE);
    }
}
