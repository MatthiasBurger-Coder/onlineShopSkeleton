package de.burger.it.application.cart.handler;

import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class OnCartCreateSaveRepository {
    private final CartRepositoryPort cartRepository;

    public OnCartCreateSaveRepository(CartRepositoryPort cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(CartCreateEvent event) {
        cartRepository.save(event.cart());
    }
}
