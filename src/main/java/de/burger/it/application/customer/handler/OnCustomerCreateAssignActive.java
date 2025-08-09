package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerCreateAssignActive {

    private final CustomerStatusAssignmentPort customerStatusAssignmentPort;

    public OnCustomerCreateAssignActive(CustomerStatusAssignmentPort customerStatusAssignmentPort) {
        this.customerStatusAssignmentPort = customerStatusAssignmentPort;
    }

    public void execute(CustomerCreateEvent event) {
        customerStatusAssignmentPort.assign(event.customer(), CustomerStateType.ACTIVE);
    }
}
