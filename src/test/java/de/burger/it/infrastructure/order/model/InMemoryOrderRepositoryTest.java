package de.burger.it.infrastructure.order.model;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.model.OrderNullObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository repository;
    private UUID id1;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
        id1 = UUID.randomUUID();
    }

    // Greenpath: save and findById existing
    @Test
    void save_and_findById_shouldReturnSavedOrder() {
        Order o = new OrderDefault(id1);
        repository.save(o);
        Order found = repository.findById(id1);
        assertEquals(id1, found.id());
        assertFalse(found.isNull());
    }

    // Redpath: findById missing returns NullObject
    @Test
    void findById_whenMissing_shouldReturnOrderNullObject() {
        Order found = repository.findById(id1);
        assertInstanceOf(OrderNullObject.class, found);
        assertTrue(found.isNull());
    }
}
