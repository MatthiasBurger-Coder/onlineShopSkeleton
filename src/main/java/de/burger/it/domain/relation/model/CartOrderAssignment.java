package de.burger.it.domain.relation.model;


import java.util.UUID;


public record CartOrderAssignment(UUID cartId, UUID orderId) {}
