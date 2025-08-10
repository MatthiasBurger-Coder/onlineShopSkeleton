package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.relation.model.CartOrderAssignment;
import de.burger.it.domain.relation.port.CartOrderAssignmentRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartOrderAssignmentAdapterTest {

    private CartOrderAssignmentAdapter adapter;
    private FakeRepo repo;

    @BeforeEach
    void setUp() {
        repo = new FakeRepo();
        adapter = new CartOrderAssignmentAdapter(repo);
    }

    // Greenpath: assign delegates to save and findAll delegates to findById
    @Test
    void assign_and_findAll_shouldPersistAndFetch() {
        UUID cartId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        var cart = new CartDefault(cartId);
        var order = new OrderDefault(orderId);

        adapter.assign(cart, order);
        CartOrderAssignment fromAdapter = adapter.findAll(cartId);
        assertNotNull(fromAdapter);
        assertEquals(cartId, fromAdapter.cartId());
        assertEquals(orderId, fromAdapter.orderId());

        // Ensure repository was called with correct ids as well
        assertEquals(orderId, repo.lastSavedOrderId);
        assertEquals(cartId, repo.lastSavedCartId);
    }

    // Redpath: if repository lacks entry, findAll returns null
    @Test
    void findAll_whenMissing_shouldReturnNull() {
        assertNull(adapter.findAll(UUID.randomUUID()));
    }

    // Minimal fake repository to observe interactions without Mockito
    static class FakeRepo implements CartOrderAssignmentRepositoryPort {
        UUID lastSavedCartId;
        UUID lastSavedOrderId;
        CartOrderAssignment stored;

        @Override
        public CartOrderAssignment findById(UUID orderId) {
            if (stored != null && stored.cartId().equals(lastSavedCartId)) {
                return stored;
            }
            return null;
        }

        @Override
        public void save(CartOrderAssignment cartOrderAssignment) {
            this.stored = cartOrderAssignment;
            this.lastSavedCartId = cartOrderAssignment.cartId();
            this.lastSavedOrderId = cartOrderAssignment.orderId();
        }
    }
}
