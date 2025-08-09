package de.burger.it.domain.cart.port;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.CartLike;

import java.util.Collection;
import java.util.UUID;

public interface CartRepositoryPort {
    CartLike findById(UUID cartId);

    void save(CartDefault cart);

    void delete(UUID cartId);

    Collection<CartLike> findAll();
}