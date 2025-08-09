package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;

/**
 * Event emitted when a new customer is created.
 */
public record CustomerCreateEvent(Customer customer) {

}
