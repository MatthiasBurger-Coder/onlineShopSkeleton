package de.burger.it.infrastructure.customer.model;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.customer.model.CustomerNullObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCustomerRepositoryTest {

    private InMemoryCustomerRepository repository;
    private UUID id1;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        id1 = UUID.randomUUID();
    }

    // Greenpath: save and findById existing
    @Test
    void save_and_findById_shouldReturnSavedCustomer() {
        Customer c = new CustomerDefault(id1, "Alice", "alice@example.com");
        repository.save(c);
        Customer found = repository.findById(id1);
        assertEquals(id1, found.id());
        assertFalse(found.isNull());
    }

    // Redpath: findById missing returns NullObject
    @Test
    void findById_whenMissing_shouldReturnCustomerNullObject() {
        Customer found = repository.findById(id1);
        assertInstanceOf(CustomerNullObject.class, found);
        assertTrue(found.isNull());
    }
}
