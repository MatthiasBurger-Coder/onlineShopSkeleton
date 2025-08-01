package de.burger.it.application.customer.handler;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerCreateNewCart {

    private final CartService cartService;

    public OnCustomerCreateNewCart(CartService cartService, CustomerStatusAssignmentPort customerStatusAssignmentPort) {
        this.cartService = cartService;
    }

    public void execute(CustomerCreateEvent event) {
        cartService.create(event.customer());
    }
}
