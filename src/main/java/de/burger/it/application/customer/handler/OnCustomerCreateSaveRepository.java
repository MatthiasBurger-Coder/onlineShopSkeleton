package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.port.CustomerRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerCreateSaveRepository {
    private final CustomerRepositoryPort customerRepository;

    public OnCustomerCreateSaveRepository(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void execute(CustomerCreateEvent event) {
        customerRepository.save(event.customer());
    }
}
