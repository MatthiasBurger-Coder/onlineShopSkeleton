package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when a customer is suspended.
 */
@Getter
public class CustomerSuspendEvent extends DomainEvent {

    private final CustomerDefault customer;

    public CustomerSuspendEvent(CustomerDefault customer) {
        this.customer = customer;
    }

}
