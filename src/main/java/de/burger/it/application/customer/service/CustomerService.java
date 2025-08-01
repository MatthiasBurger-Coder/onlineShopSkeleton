package de.burger.it.application.customer.service;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerState;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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
        customerStatusAssignmentPort.assign(customer, CustomerStateType.CREATE);
        publisher.publishEvent(new CustomerCreateEvent(customer));
    }

    public void suspendCustomer(Customer customer) {
        publisher.publishEvent(new CustomerSuspendEvent(customer));
        var cartList = cartService.findAllCartByCustomer(customer);
        cartList.forEach(cart -> cartService.close(cart, customer));
    }

    public CustomerState getState(Customer customer) {
        return customerStatusAssignmentPort.findBy(customer.id()).toState();
    }
}
