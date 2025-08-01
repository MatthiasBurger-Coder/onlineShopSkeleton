package de.burger.it.domain.customer.model;

import de.burger.it.domain.customer.state.CustomerStateType;

import java.util.UUID;

public record CustomerStatusAssignment(UUID customerId,
                                       CustomerStateType state) {
}
