package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerSuspendAssignSuspend {

    private final CustomerStatusAssignmentPort customerStatusAssignment;

    public OnCustomerSuspendAssignSuspend(CustomerStatusAssignmentPort customerStatusAssignment) {
        this.customerStatusAssignment = customerStatusAssignment;
    }

    public void execute(CustomerSuspendEvent event) {
        customerStatusAssignment.assign(event.customer(), CustomerStateType.SUSPENDED);
    }
}