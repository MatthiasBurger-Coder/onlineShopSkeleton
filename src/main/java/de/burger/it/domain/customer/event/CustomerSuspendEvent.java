package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when a customer is suspended.
 */
@Getter
public class CustomerSuspendEvent extends DomainEvent {

    private final Customer customer;

    public CustomerSuspendEvent(Customer customer) {
        this.customer = customer;
    }

}
