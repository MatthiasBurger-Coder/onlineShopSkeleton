package de.burger.it.application.customer.service;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.cart.model.CartLike;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerLike;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerState;
import de.burger.it.domain.customer.state.CustomerStateType;
import de.burger.it.domain.customer.state.NullCustomerState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void createNewCustomer(CustomerLike customer) {
        if (customer.isNull()) {
            return;
        }
        customerStatusAssignmentPort.assign((Customer) customer, CustomerStateType.CREATE);
        publisher.publishEvent(new CustomerCreateEvent((Customer) customer));
    }

    public void suspendCustomer(CustomerLike customer) {
        if (customer.isNull()) {
            return;
        }
        customerStatusAssignmentPort.assign((Customer) customer, CustomerStateType.SUSPENDED);
        publisher.publishEvent(new CustomerSuspendEvent((Customer) customer));
        List<CartLike> cartList = cartService.findAllCartByCustomer(customer);
        cartList.forEach(cart -> cartService.close(cart, customer));
    }

    public CustomerState getState(CustomerLike customer) {
        if (customer.isNull()) {
            return NullCustomerState.getInstance();
        }
        var stateType = customerStatusAssignmentPort.findBy(customer.id());
        return stateType != null ? stateType.toState() : NullCustomerState.getInstance();
    }
}
