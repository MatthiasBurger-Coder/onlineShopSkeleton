package de.burger.it.domain.relation.model;


import java.util.UUID;


public record CartCustomerAssignment(UUID cartId, UUID customerId) {
}
