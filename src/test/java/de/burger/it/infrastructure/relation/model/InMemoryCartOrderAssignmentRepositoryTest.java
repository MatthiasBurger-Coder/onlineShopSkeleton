package de.burger.it.infrastructure.relation.model;

import de.burger.it.domain.relation.model.CartOrderAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCartOrderAssignmentRepositoryTest {

    private InMemoryCartOrderAssignmentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCartOrderAssignmentRepository();
    }

    // Greenpath: save and findById existing
    @Test
    void save_and_findById_shouldReturnSavedAssignment() {
        UUID cartId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        CartOrderAssignment assignment = new CartOrderAssignment(cartId, orderId);
        repository.save(assignment);
        CartOrderAssignment found = repository.findById(orderId);
        assertNotNull(found);
        assertEquals(cartId, found.cartId());
        assertEquals(orderId, found.orderId());
    }

    // Redpath: findById missing returns null
    @Test
    void findById_whenMissing_shouldReturnNull() {
        assertNull(repository.findById(UUID.randomUUID()));
    }
}
