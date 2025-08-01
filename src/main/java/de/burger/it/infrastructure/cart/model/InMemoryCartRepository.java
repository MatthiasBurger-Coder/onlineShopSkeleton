package de.burger.it.infrastructure.cart.model;

import de.burger.it.domain.cart.port.CartRepositoryPort;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartLike;
import de.burger.it.domain.cart.model.NullCart;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryCartRepository implements CartRepositoryPort {

    private final Map<UUID, Cart> store = new HashMap<>();

    @Override
    public CartLike findById(UUID cartId) {
        Cart cart = store.get(cartId);
        return cart != null ? cart : NullCart.getInstance();
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
