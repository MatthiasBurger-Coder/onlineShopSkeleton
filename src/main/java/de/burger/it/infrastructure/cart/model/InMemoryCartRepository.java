package de.burger.it.infrastructure.cart.model;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartNullObject;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCartRepository implements CartRepositoryPort {

    private final Map<UUID, Cart> store = new ConcurrentHashMap<>();

    @Override
    public Cart findById(UUID cartId) {
        return Optional.ofNullable(store.get(cartId))
                .orElse(CartNullObject.getInstance());
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
        return new ArrayList<>(store.values());
    }
}
