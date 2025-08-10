package de.burger.it.domain.cart.state;

import de.burger.it.domain.cart.model.CartNullObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullCartStateEqualsTest {

    @Test
    @DisplayName("Greenpath: equals with same type and CartNullObject, and hashCode is constant")
    void equals_positiveCases_Greenpath() {
        NullCartState a = new NullCartState();
        NullCartState b = new NullCartState();
        assertEquals(a, b);
        assertEquals(0, a.hashCode());
        assertEquals(a, CartNullObject.getInstance());
    }

    @Test
    @DisplayName("Redpath: equals(null) and equals(unrelated) should be false")
    void equals_negativeCases_Redpath() {
        NullCartState a = new NullCartState();
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }
}
