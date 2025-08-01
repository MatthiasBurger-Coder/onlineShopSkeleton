package de.burger.it.infrastructure.cart.model;

import de.burger.it.domain.cart.port.CartRepository;
import de.burger.it.domain.cart.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryCartRepository implements CartRepository {

    private final Map<UUID, Cart> store = new HashMap<>();

    @Override
    public Cart findById(UUID cartId) {
        return store.get(cartId);
    }

    @Override
    public void save(Cart cart) {
        store.put(cart.id(), cart);
    }

    @Override
    public void delete(UUID cartId) {
        store.remove(cartId);
    }
    @Override
    public Collection<Cart> findAll() {
        return store.values();
    }
}
