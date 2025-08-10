package de.burger.it.application.customer.service

import de.burger.it.application.cart.service.CartService
import de.burger.it.domain.customer.event.CustomerCreateEvent
import de.burger.it.domain.customer.model.Customer
import de.burger.it.domain.customer.model.CustomerDefault
import de.burger.it.domain.customer.model.CustomerNullObject
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort
import de.burger.it.domain.customer.state.CustomerStateType
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

@Title("Use Case: Create a new customer")
@Narrative(
        """
        As a product owner
        I want to create a new customer
        so that they are active immediately and a shopping cart is created.
        """
)
class CustomerCreationSpec extends Specification {

    def "BBT: New customer is created successfully and event is published"() {
        given: "A valid customer and infrastructure adapters as mocks"
        def statusPort = Mock(CustomerStatusAssignmentPort)
        def publisher = Mock(ApplicationEventPublisher)
        def cartService = Mock(CartService)
        def service = new CustomerService(statusPort, publisher, cartService)
        Customer customer = new CustomerDefault(UUID.randomUUID(), "Alice", "alice@example.org")

        when: "The customer is created"
        service.createNewCustomer(customer)

        then: "First the CREATE state is assigned"
        1 * statusPort.assign(customer, CustomerStateType.CREATE)

        and: "Then a CustomerCreateEvent is published"
        1 * publisher.publishEvent({ CustomerCreateEvent e -> e.customer().is(customer) })

        and: "Shopping cart creation happens later in the process pipeline (not directly in the service)"
        0 * cartService._
    }

    def "BBT: No creation for null input"() {
        given:
        def statusPort = Mock(CustomerStatusAssignmentPort)
        def publisher = Mock(ApplicationEventPublisher)
        def cartService = Mock(CartService)
        def service = new CustomerService(statusPort, publisher, cartService)

        when:
        service.createNewCustomer(null)

        then: "No interactions happen"
        0 * statusPort._
        0 * publisher._
        0 * cartService._
    }

    def "BBT: No creation for NullObject customer"() {
        given:
        def statusPort = Mock(CustomerStatusAssignmentPort)
        def publisher = Mock(ApplicationEventPublisher)
        def cartService = Mock(CartService)
        def service = new CustomerService(statusPort, publisher, cartService)
        Customer customer = CustomerNullObject.getInstance()

        when:
        service.createNewCustomer(customer)

        then:
        0 * statusPort._
        0 * publisher._
        0 * cartService._
    }
}
