package de.burger.it.domain.cart.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartNullObjectAdditionalEqualsTest {

    @Test
    @DisplayName("Redpath: equals(null) and equals(unrelated) should be false")
    void equals_withNullAndUnrelated_Redpath() {
        CartNullObject nullCart = (CartNullObject) CartNullObject.getInstance();
        assertNotEquals(nullCart, null);
        assertNotEquals(nullCart, new Object());
    }
}
