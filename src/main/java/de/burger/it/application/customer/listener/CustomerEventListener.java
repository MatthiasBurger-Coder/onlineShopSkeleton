package de.burger.it.application.customer.listener;

import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener {

    private final ProcessPipeline<CustomerCreateEvent> customerCreatePipeline;
    private final ProcessPipeline<CustomerSuspendEvent> customerSuspendPipeline;

    public CustomerEventListener(ProcessPipeline<CustomerCreateEvent> customerCreatePipeline,
                                 ProcessPipeline<CustomerSuspendEvent> customerSuspendPipeline) {
        this.customerCreatePipeline = customerCreatePipeline;
        this.customerSuspendPipeline = customerSuspendPipeline;
    }

    @EventListener
    public void onCustomerCreate(CustomerCreateEvent event) {
        customerCreatePipeline.execute(event);
    }

    @EventListener
    public void onCustomerSuspend(CustomerSuspendEvent event) {
        customerSuspendPipeline.execute(event);
    }
}


