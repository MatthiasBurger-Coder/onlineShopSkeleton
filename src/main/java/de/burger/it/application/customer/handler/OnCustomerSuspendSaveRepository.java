package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.port.CustomerRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerSuspendSaveRepository {
    private final CustomerRepositoryPort customerRepository;

    public OnCustomerSuspendSaveRepository(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void execute(CustomerSuspendEvent event) {
        customerRepository.save(event.customer());
    }
}
