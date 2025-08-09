package de.burger.it.domain.cart.model;

import java.util.Optional;
import java.util.UUID;

public record Cart(UUID id) implements CartLike {

    public Cart {
        id = Optional.ofNullable(id)
                .orElseThrow(() -> new IllegalArgumentException("id cannot be null"));
    }
}
