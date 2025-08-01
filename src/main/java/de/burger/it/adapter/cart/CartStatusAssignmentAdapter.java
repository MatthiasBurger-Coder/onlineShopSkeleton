package de.burger.it.adapter.cart;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.state.CartStateType;
import de.burger.it.domain.cart.model.CartStatusAssignment;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.infrastructure.cart.port.CartStatusAssignmentRepository;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Component
public class CartStatusAssignmentAdapter implements CartStatusAssignmentPort {
    private final CartStatusAssignmentRepository repository;

    public CartStatusAssignmentAdapter(CartStatusAssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void assign(Cart cart, CartStateType newState) {
        var assignment = new CartStatusAssignment(
                cart.id(),
                newState,
                LocalDateTime.now(),
                null
        );
        repository.save(assignment);
    }

    @Override
    public CartStateType findBy(UUID cartId) {
        return repository.findAllById(cartId).state();
    }
}
