package de.burger.it.infrastructure.cart.model;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.CartNullObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCartRepositoryTest {

    private InMemoryCartRepository repository;
    private UUID id1;
    private UUID id2;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCartRepository();
        id1 = UUID.randomUUID();
        id2 = UUID.randomUUID();
    }

    // Greenpath: save and findById existing
    @Test
    void save_and_findById_shouldReturnSavedCart() {
        Cart cart = new CartDefault(id1);
        repository.save(cart);
        Cart found = repository.findById(id1);
        assertEquals(id1, found.id());
        assertFalse(found.isNull());
    }

    // Redpath: findById missing returns NullObject
    @Test
    void findById_whenMissing_shouldReturnCartNullObject() {
        Cart found = repository.findById(id1);
        assertInstanceOf(CartNullObject.class, found);
        assertTrue(found.isNull());
    }

    // Greenpath: save two and findAll returns both
    @Test
    void findAll_shouldReturnAllSavedCarts() {
        repository.save(new CartDefault(id1));
        repository.save(new CartDefault(id2));
        Collection<Cart> all = repository.findAll();
        assertEquals(2, all.size());
    }

    // Green/Red: delete removes entry; deleting non-existent has no effect
    @Test
    void delete_shouldRemoveCartAndBeIdempotent() {
        repository.save(new CartDefault(id1));
        assertFalse(repository.findById(id1).isNull());
        repository.delete(id1);
        assertTrue(repository.findById(id1).isNull());
        // idempotent
        repository.delete(id1);
        assertTrue(repository.findById(id1).isNull());
    }
}
