package de.burger.it.application.config;

import de.burger.it.application.order.handler.OnOrderCancelAssignCanceledState;
import de.burger.it.application.order.handler.OnOrderCreateAssignNewState;
import de.burger.it.application.order.handler.OnOrderCreateSaveRepository;
import de.burger.it.application.order.handler.OnOrderDeliverAssignDeliveredState;
import de.burger.it.application.order.handler.OnOrderPayAssignPaidState;
import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderProcessConfig {
    
    @Bean
    public ProcessPipeline<OrderCreateEvent> orderCreateProcessPipeline(
            OnOrderCreateAssignNewState assignNewState,
            OnOrderCreateSaveRepository saveRepository) {
        return new ProcessPipeline<OrderCreateEvent>()
                .append(assignNewState::execute)
                .append(saveRepository::execute);
    }
    
    @Bean
    public ProcessPipeline<OrderPayEvent> orderPayProcessPipeline(
            OnOrderPayAssignPaidState assignPaidState) {
        return new ProcessPipeline<OrderPayEvent>()
                .append(assignPaidState::execute);
    }
    
    @Bean
    public ProcessPipeline<OrderCancelEvent> orderCancelProcessPipeline(
            OnOrderCancelAssignCanceledState assignCanceledState) {
        return new ProcessPipeline<OrderCancelEvent>()
                .append(assignCanceledState::execute);
    }
    
    @Bean
    public ProcessPipeline<OrderDeliverEvent> orderDeliverProcessPipeline(
            OnOrderDeliverAssignDeliveredState assignDeliveredState) {
        return new ProcessPipeline<OrderDeliverEvent>()
                .append(assignDeliveredState::execute);
    }
}