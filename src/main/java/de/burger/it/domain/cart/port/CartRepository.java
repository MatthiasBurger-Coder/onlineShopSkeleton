package de.burger.it.domain.cart.port;

import de.burger.it.domain.cart.model.Cart;

import java.util.Collection;
import java.util.UUID;

public interface CartRepository {
    Cart findById(UUID cartId);

    void save(Cart cart);

    void delete(UUID cartId);

    Collection<Cart> findAll();
}
