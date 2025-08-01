package de.burger.it.adapter.relation;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import de.burger.it.infrastructure.relation.port.CartCustomerAssignmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CartCustomerAssignmentAdapter implements CartCustomerAssignmentPort {

    private final CartCustomerAssignmentRepository repository;

    public CartCustomerAssignmentAdapter(CartCustomerAssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void assign(Cart cart, Customer customer) {
        var ownership = new CartCustomerAssignment (
                cart.id(),
                customer.id()
        );
        repository.save(ownership);
    }

    @Override
    public List<CartCustomerAssignment> findAllByCard(UUID cartId) {
        return repository.findAllByCartId(cartId);
    }

    @Override
    public List<CartCustomerAssignment> findAllByCustomer(UUID customerId) {
        return repository.findAllByCustumerId(customerId);
    }


}

