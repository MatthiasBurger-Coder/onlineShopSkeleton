package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCartCreateAssignCartStatus {

    private final CartStatusAssignmentPort cartStatusAssignmentPort;

    public OnCartCreateAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentPort) {
        this.cartStatusAssignmentPort = cartStatusAssignmentPort;
    }

    public void execute(CartCreateEvent event) {
        cartStatusAssignmentPort.assign(event.cart(), CartStateType.CREATED);
    }
}
