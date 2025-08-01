package de.burger.it.domain.customer.model;

import java.util.UUID;

public record Customer(UUID id, String name, String email) {
}
