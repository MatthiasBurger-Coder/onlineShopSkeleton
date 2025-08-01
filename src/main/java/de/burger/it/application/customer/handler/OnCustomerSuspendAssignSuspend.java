package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

@Component
public class OnCustomerSuspendAssignSuspend {

    private final CustomerStatusAssignmentPort customerStatusAssignmentPort;

    public OnCustomerSuspendAssignSuspend(CustomerStatusAssignmentPort customerStatusAssignmentPort) {
        this.customerStatusAssignmentPort = customerStatusAssignmentPort;
    }

    public void execute(CustomerSuspendEvent event) {
        customerStatusAssignmentPort.assign(event.customer(), CustomerStateType.SUSPENDED);
    }
}
