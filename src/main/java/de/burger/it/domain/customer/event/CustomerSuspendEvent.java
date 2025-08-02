package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.common.event.DomainEvent;

/**
 * Event emitted when a customer is suspended.
 */
public class CustomerSuspendEvent extends DomainEvent {

    private final Customer customer;

    public CustomerSuspendEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
