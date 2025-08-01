package de.burger.it.application.cart.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCartActiveAssignCartStatus implements StateEventHandler<CartStateType, CartActiveEvent> {

    private final CartStatusAssignmentPort cartStatusAssignmentPort;

    public OnCartActiveAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentPort) {
        this.cartStatusAssignmentPort = cartStatusAssignmentPort;
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
        cartStatusAssignmentPort.assign(event.cart(), CartStateType.ACTIVE);
    }
}
