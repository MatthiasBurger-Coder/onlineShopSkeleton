package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.infrastructure.customer.port.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerCreateSaveRepository {
    private final CustomerRepository customerRepository;

    public OnCustomerCreateSaveRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void execute(CustomerCreateEvent event) {
        customerRepository.save(event.getCustomer());
    }
}
