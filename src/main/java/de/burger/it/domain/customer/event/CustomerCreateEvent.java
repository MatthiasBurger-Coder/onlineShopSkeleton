package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;

public record CustomerCreateEvent (Customer customer) { }
