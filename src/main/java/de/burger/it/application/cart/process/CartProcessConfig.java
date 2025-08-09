package de.burger.it.application.cart.process;

import de.burger.it.application.cart.handler.OnCartActiveAssignCartStatus;
import de.burger.it.application.cart.handler.OnCartCloseAssignCartStatus;
import de.burger.it.application.cart.handler.OnCartCreateAssignCartStatus;
import de.burger.it.application.cart.handler.OnCartCreateAssignCustomer;
import de.burger.it.application.cart.handler.OnCartCreateSaveRepository;
import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartProcessConfig {
    
    @Bean
    public ProcessPipeline<CartCreateEvent> cartCreateProcessPipeline(
            OnCartCreateAssignCartStatus assignCartStatus,
            OnCartCreateAssignCustomer assignCustomer,
            OnCartCreateSaveRepository saveRepository) {
        return new ProcessPipeline<CartCreateEvent>()
                .append(assignCartStatus::execute)
                .append(assignCustomer::execute)
                .append(saveRepository::execute);
    }
    
    @Bean
    public ProcessPipeline<CartActiveEvent> cartActiveProcessPipeline(
            OnCartActiveAssignCartStatus assignCartStatus) {
        return new ProcessPipeline<CartActiveEvent>()
                .append(assignCartStatus::execute);
    }
    
    @Bean
    public ProcessPipeline<CartCloseEvent> cartCloseProcessPipeline(
            OnCartCloseAssignCartStatus assignCartStatus) {
        return new ProcessPipeline<CartCloseEvent>()
                .append(assignCartStatus::execute);
    }
}