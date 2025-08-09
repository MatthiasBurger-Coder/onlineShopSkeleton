package de.burger.it.domain.cart.port;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.Cart;

import java.util.Collection;
import java.util.UUID;

public interface CartRepositoryPort {
    Cart findById(UUID cartId);

    void save(CartDefault cart);

    void delete(UUID cartId);

    Collection<Cart> findAll();
}