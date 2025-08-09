package de.burger.it.infrastructure.cart.model;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartNullObject;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCartRepository implements CartRepositoryPort {

    private final Map<UUID, CartDefault> store = new ConcurrentHashMap<>();

    @Override
    public Cart findById(UUID cartId) {
        return Optional.ofNullable(store.get(cartId))
                .map(cart -> (Cart) cart)
                .orElse(CartNullObject.getInstance());
    }

    @Override
    public void save(CartDefault cart) {
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
