package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCartCloseAssignCartStatus {

    private final CartStatusAssignmentPort cartStatusAssignmentPort;

    public OnCartCloseAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentPort) {
        this.cartStatusAssignmentPort = cartStatusAssignmentPort;
    }

    public void execute(CartCloseEvent event) {
        cartStatusAssignmentPort.assign(event.getCart(), CartStateType.CLOSED);
    }
}
