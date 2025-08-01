package de.burger.it.application.cart.listener;

import de.burger.it.application.dispatcher.StateEventDispatcher;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CartEventListener {

    private final StateEventDispatcher dispatcher;
    private final CartStatusAssignmentPort cartStatusAssignmentAdapter;

    public CartEventListener(StateEventDispatcher dispatcher, CartStatusAssignmentPort cartStatusAssignmentAdapter) {
        this.dispatcher = dispatcher;
        this.cartStatusAssignmentAdapter = cartStatusAssignmentAdapter;
    }

    @EventListener
    public void handleCartCreated(CartCreateEvent event) {
        dispatcher.dispatch(CartStateType.CREATED, event);
    }

    @EventListener
    public void handleCartClose(CartCloseEvent event) {
        var assingment = cartStatusAssignmentAdapter.findBy(event.cart().id());
        dispatcher.dispatch(assingment, event);
    }

    @EventListener
    public void handleCartActivate(CartActiveEvent event) {
        var assingment = cartStatusAssignmentAdapter.findBy(event.cart().id());
        dispatcher.dispatch(assingment, event);
    }
}
