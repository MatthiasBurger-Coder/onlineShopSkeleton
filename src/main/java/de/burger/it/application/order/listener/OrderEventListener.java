package de.burger.it.application.order.listener;

import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final ProcessPipeline<OrderCreateEvent> orderCreatePipeline;
    private final ProcessPipeline<OrderPayEvent> orderPayPipeline;
    private final ProcessPipeline<OrderCancelEvent> orderCancelPipeline;
    private final ProcessPipeline<OrderDeliverEvent> orderDeliverPipeline;

    public OrderEventListener(ProcessPipeline<OrderCreateEvent> orderCreatePipeline,
                              ProcessPipeline<OrderPayEvent> orderPayPipeline,
                              ProcessPipeline<OrderCancelEvent> orderCancelPipeline,
                              ProcessPipeline<OrderDeliverEvent> orderDeliverPipeline) {
        this.orderCreatePipeline = orderCreatePipeline;
        this.orderPayPipeline = orderPayPipeline;
        this.orderCancelPipeline = orderCancelPipeline;
        this.orderDeliverPipeline = orderDeliverPipeline;
    }

    @EventListener
    public void handleOrderCreate(OrderCreateEvent event) {
        orderCreatePipeline.execute(event);
    }

    @EventListener
    public void handleOrderPay(OrderPayEvent event) {
        orderPayPipeline.execute(event);
    }

    @EventListener
    public void handleOrderCancel(OrderCancelEvent event) {
        orderCancelPipeline.execute(event);
    }

    @EventListener
    public void handleOrderDeliver(OrderDeliverEvent event) {
        orderDeliverPipeline.execute(event);
    }
}