package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;

/**
 * Event emitted when a customer is suspended.
 */
public record CustomerSuspendEvent(Customer customer) {

}
