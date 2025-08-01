package de.burger.it.domain.cart.model;

import java.util.UUID;

public record Cart(UUID id) implements CartLike {
    
    @Override
    public boolean isNull() {
        return false;
    }
}
