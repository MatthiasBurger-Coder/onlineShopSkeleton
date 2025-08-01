package de.burger.it.application.cart.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.state.CartStateType;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCartCreateAssignCustomer implements StateEventHandler<CartStateType, CartCreateEvent> {

    private final CartCustomerAssignmentPort cartCustomerAssignmentPort;

    public OnCartCreateAssignCustomer(CartCustomerAssignmentPort cartCustomerAssignmentPort) {
        this.cartCustomerAssignmentPort = cartCustomerAssignmentPort;
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
        cartCustomerAssignmentPort.assign(event.cart(),event.customer());
    }
}
