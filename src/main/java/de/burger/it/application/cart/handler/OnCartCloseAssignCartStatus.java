package de.burger.it.application.cart.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCartCloseAssignCartStatus implements StateEventHandler<CartStateType, CartCloseEvent> {

    private final CartStatusAssignmentPort cartStatusAssignmentPort;

    public OnCartCloseAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentPort) {
        this.cartStatusAssignmentPort = cartStatusAssignmentPort;
    }

    @Override
    public Collection<CartStateType> supportedStates() {
        return List.of(CartStateType.ACTIVE,CartStateType.CREATED,CartStateType.CLOSED);
    }

    @Override
    public Class<CartCloseEvent> supportedEvent() {
        return CartCloseEvent.class;
    }

    @Override
    public void execute(CartCloseEvent event) {
        cartStatusAssignmentPort.assign(event.cart(), CartStateType.CLOSED);
    }
}
