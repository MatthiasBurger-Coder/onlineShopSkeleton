package de.burger.it.domain.relation.port;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.model.CartCustomerAssignment;

import java.util.List;
import java.util.UUID;

public interface CartCustomerAssignmentPort {
    List<CartCustomerAssignment> findAllByCart(UUID cartId);
    List<CartCustomerAssignment> findAllByCustomer(UUID customerId);
    void assign(Cart cart, Customer customer);
}
