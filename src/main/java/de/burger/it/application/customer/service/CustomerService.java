package de.burger.it.application.customer.service;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerState;
import de.burger.it.domain.customer.state.CustomerStateType;
import de.burger.it.domain.customer.state.NullCustomerState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomerService {

    private final ApplicationEventPublisher publisher;
    private final CustomerStatusAssignmentPort customerStatusAssignmentPort;
    private final CartService cartService;

    public CustomerService(CustomerStatusAssignmentPort customerStatusAssignmentPort, ApplicationEventPublisher publisher, CartService cartService) {
        this.customerStatusAssignmentPort = customerStatusAssignmentPort;
        this.publisher = publisher;
        this.cartService = cartService;
    }

    public void createNewCustomer(Customer customer) {
        Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .ifPresent(c -> {
                    customerStatusAssignmentPort.assign(c, CustomerStateType.CREATE);
                    publisher.publishEvent(new CustomerCreateEvent(c));
                });
    }

    public void suspendCustomer(Customer customer) {
        Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .ifPresent(c -> {
                    customerStatusAssignmentPort.assign(c, CustomerStateType.SUSPENDED);
                    publisher.publishEvent(new CustomerSuspendEvent(c));
                    Optional.ofNullable(cartService.findAllCartByCustomer(c))
                            .orElse(Collections.emptyList())
                            .forEach(cart -> cartService.close(cart, c));
                });
    }

    public CustomerState getState(Customer customer) {
        return Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .map(c -> customerStatusAssignmentPort.findBy(c.id()))
                .map(CustomerStateType::toState)
                .orElse(NullCustomerState.getInstance());
    }
}
