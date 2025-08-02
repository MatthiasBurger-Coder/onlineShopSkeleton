package de.burger.it.domain.customer.model;

import java.util.UUID;

public record Customer(UUID id, String name, String email) implements CustomerLike {

    public Customer {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or blank");
        }
    }
    
    @Override
    public boolean isNull() {
        return false;
    }
}
