package de.burger.it.application.cart.listener;

import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CartEventListener {

    private final ProcessPipeline<CartCreateEvent> cartCreatePipeline;
    private final ProcessPipeline<CartActiveEvent> cartActivePipeline;
    private final ProcessPipeline<CartCloseEvent> cartClosePipeline;

    public CartEventListener(ProcessPipeline<CartCreateEvent> cartCreatePipeline,
                             ProcessPipeline<CartActiveEvent> cartActivePipeline,
                             ProcessPipeline<CartCloseEvent> cartClosePipeline) {
        this.cartCreatePipeline = cartCreatePipeline;
        this.cartActivePipeline = cartActivePipeline;
        this.cartClosePipeline = cartClosePipeline;
    }

    @EventListener
    public void handleCartCreated(CartCreateEvent event) {
        cartCreatePipeline.execute(event);
    }

    @EventListener
    public void handleCartClose(CartCloseEvent event) {
        cartClosePipeline.execute(event);
    }

    @EventListener
    public void handleCartActivate(CartActiveEvent event) {
        cartActivePipeline.execute(event);
    }
}
