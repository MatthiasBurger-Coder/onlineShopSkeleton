package de.burger.it.application.customer.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.state.CustomerStateType;
import de.burger.it.infrastructure.customer.port.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCustomerCreateSaveRepository implements StateEventHandler<CustomerStateType, CustomerCreateEvent> {
    private final CustomerRepository customerRepository;

    public OnCustomerCreateSaveRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Collection<CustomerStateType> supportedStates() {
        return List.of(CustomerStateType.ACTIVE);
    }

    @Override
    public Class<CustomerCreateEvent> supportedEvent() {
        return CustomerCreateEvent.class;
    }

    @Override
    public void execute(CustomerCreateEvent event) {
        customerRepository.save(event.customer());
    }
}
