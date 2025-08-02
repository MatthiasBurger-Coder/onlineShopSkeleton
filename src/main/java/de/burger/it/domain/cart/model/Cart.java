package de.burger.it.domain.cart.model;

import java.util.UUID;

public record Cart(UUID id) implements CartLike {

    public Cart {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }
    
    @Override
    public boolean isNull() {
        return false;
    }
}
