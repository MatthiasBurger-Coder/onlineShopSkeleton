package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when a new customer is created.
 */
@Getter
public class CustomerCreateEvent extends DomainEvent {

    private final CustomerDefault customer;

    public CustomerCreateEvent(CustomerDefault customer) {
        this.customer = customer;
    }

}
