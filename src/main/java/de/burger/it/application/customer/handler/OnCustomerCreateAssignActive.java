package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerCreateAssignActive {

    private final CustomerStatusAssignmentPort customerStatusAssignment;

    public OnCustomerCreateAssignActive(CustomerStatusAssignmentPort customerStatusAssignment) {
        this.customerStatusAssignment = customerStatusAssignment;
    }

    public void execute(CustomerCreateEvent event) {
        customerStatusAssignment.assign(event.customer(), CustomerStateType.ACTIVE);
    }
}
