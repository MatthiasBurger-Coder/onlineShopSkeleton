package de.burger.it.domain.customer.model;

import java.util.Optional;
import java.util.UUID;

public record CustomerDefault(UUID id, String name, String email) implements Customer {

    public CustomerDefault {
        id = Optional.ofNullable(id)
                .orElseThrow(() -> new IllegalArgumentException("id cannot be null"));
        name = Optional.ofNullable(name)
                .filter(n -> !n.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("name cannot be null or blank"));
        email = Optional.ofNullable(email)
                .filter(e -> !e.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("email cannot be null or blank"));
    }
}
