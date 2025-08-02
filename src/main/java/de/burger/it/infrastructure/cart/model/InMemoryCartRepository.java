package de.burger.it.infrastructure.cart.model;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartLike;
import de.burger.it.domain.cart.model.NullCart;
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

    private final Map<UUID, Cart> store = new ConcurrentHashMap<>();

    @Override
    public CartLike findById(UUID cartId) {
        return Optional.ofNullable(store.get(cartId))
                .map(cart -> (CartLike) cart)
                .orElse(NullCart.getInstance());
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
    public Collection<CartLike> findAll() {
        return new ArrayList<>(store.values());
    }
}
