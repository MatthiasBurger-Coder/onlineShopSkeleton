package de.burger.it.application.customer.handler;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCustomerCreateNewCart implements StateEventHandler<CustomerStateType, CustomerCreateEvent> {

    private final CartService cartService;

    public OnCustomerCreateNewCart(CartService cartService, CustomerStatusAssignmentPort customerStatusAssignmentPort) {
        this.cartService = cartService;
    }

    @Override
    public Collection<CustomerStateType> supportedStates() {
        return List.of(CustomerStateType.CREATE);
    }

    @Override
    public Class<CustomerCreateEvent> supportedEvent() {
        return CustomerCreateEvent.class;
    }

    @Override
    public void execute(CustomerCreateEvent event) {
        cartService.create(event.customer());
    }
}
