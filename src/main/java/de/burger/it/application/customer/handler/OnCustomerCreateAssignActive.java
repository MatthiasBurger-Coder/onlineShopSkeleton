package de.burger.it.application.customer.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCustomerCreateAssignActive implements StateEventHandler<CustomerStateType, CustomerCreateEvent> {

    private final CustomerStatusAssignmentPort customerStatusAssignmentPort;

    public OnCustomerCreateAssignActive(CustomerStatusAssignmentPort customerStatusAssignmentPort) {
        this.customerStatusAssignmentPort = customerStatusAssignmentPort;
    }

    @Override
    public Collection<CustomerStateType> supportedStates() {
        return List.of(CustomerStateType.ACTIVE, CustomerStateType.CREATE);
    }

    @Override
    public Class<CustomerCreateEvent> supportedEvent() {
        return CustomerCreateEvent.class;
    }

    @Override
    public void execute(CustomerCreateEvent event) {
        customerStatusAssignmentPort.assign(event.customer(), CustomerStateType.ACTIVE);
    }
}
