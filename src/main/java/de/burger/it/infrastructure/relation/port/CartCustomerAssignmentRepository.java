package de.burger.it.infrastructure.relation.port;


import de.burger.it.domain.relation.model.CartCustomerAssignment;

import java.util.List;
import java.util.UUID;

public interface CartCustomerAssignmentRepository {
    List<CartCustomerAssignment> findAllByCustumerId(UUID customerId);
    List<CartCustomerAssignment> findAllByCartId(UUID cartId);

    void save(CartCustomerAssignment cartCustomerAssignment);

}
