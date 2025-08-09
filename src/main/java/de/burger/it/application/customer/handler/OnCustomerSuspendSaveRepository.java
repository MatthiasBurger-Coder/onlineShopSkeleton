package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.port.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerSuspendSaveRepository {
    private final CustomerRepository customerRepository;

    public OnCustomerSuspendSaveRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void execute(CustomerSuspendEvent event) {
        customerRepository.save(event.getCustomer());
    }
}
