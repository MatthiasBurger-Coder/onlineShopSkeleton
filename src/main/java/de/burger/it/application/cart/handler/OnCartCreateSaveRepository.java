package de.burger.it.application.cart.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCartCreateSaveRepository implements StateEventHandler<CartStateType, CartCreateEvent> {
    private final CartRepositoryPort cartRepository;

    public OnCartCreateSaveRepository(CartRepositoryPort cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Collection<CartStateType> supportedStates() {
        return List.of(CartStateType.CREATED);
    }

    @Override
    public Class<CartCreateEvent> supportedEvent() {
        return CartCreateEvent.class;
    }

    @Override
    public void execute(CartCreateEvent event) {
        cartRepository.save(event.cart());
    }
}
