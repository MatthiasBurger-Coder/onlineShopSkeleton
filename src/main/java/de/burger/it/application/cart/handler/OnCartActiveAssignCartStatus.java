package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCartActiveAssignCartStatus {

    private final CartStatusAssignmentPort cartStatusAssignmentPort;

    public OnCartActiveAssignCartStatus(CartStatusAssignmentPort cartStatusAssignmentPort) {
        this.cartStatusAssignmentPort = cartStatusAssignmentPort;
    }

    public void execute(CartActiveEvent event) {
        cartStatusAssignmentPort.assign(event.cart(), CartStateType.ACTIVE);
    }
}
