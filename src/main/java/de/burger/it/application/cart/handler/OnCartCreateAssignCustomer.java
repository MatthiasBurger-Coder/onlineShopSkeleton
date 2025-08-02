package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import org.springframework.stereotype.Component;

@Component
public class OnCartCreateAssignCustomer {

    private final CartCustomerAssignmentPort cartCustomerAssignmentPort;

    public OnCartCreateAssignCustomer(CartCustomerAssignmentPort cartCustomerAssignmentPort) {
        this.cartCustomerAssignmentPort = cartCustomerAssignmentPort;
    }

    public void execute(CartCreateEvent event) {
        cartCustomerAssignmentPort.assign(event.getCart(), event.getCustomer());
    }
}
