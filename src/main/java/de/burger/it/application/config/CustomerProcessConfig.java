package de.burger.it.application.config;

import de.burger.it.application.customer.handler.*;
import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerProcessConfig {
    @Bean
    public ProcessPipeline<CustomerCreateEvent> customerCreateProcessPipeline(
            OnCustomerCreateAssignActive assignActive,
            OnCustomerCreateSaveRepository saveRepository,
            OnCustomerCreateNewCart createNewCart) {

        return new ProcessPipeline<CustomerCreateEvent>()
                .append(assignActive::execute)
                .append(saveRepository::execute)
                .append(createNewCart::execute);
    }

    @Bean
    public ProcessPipeline<CustomerSuspendEvent> customerSuspendProcessPipeline(
            OnCustomerSuspendAssignSuspend assignSuspend,
            OnCustomerSuspendSaveRepository saveRepository
    ) {
        return new ProcessPipeline<CustomerSuspendEvent>()
                .append(assignSuspend::execute)
                .append(saveRepository::execute);
    }
}
