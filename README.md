# State Management System

A sophisticated state management system for e-commerce applications, demonstrating advanced design patterns and event-driven architecture. This project showcases how to implement complex state transitions for shopping carts, customers, and orders using domain-driven design principles.

**Version:** 2.0-SNAPSHOT

## Features

- **State Management**: Robust state machine implementation for carts, customers, and orders
- **Event-Driven Architecture**: Event-based system for handling state transitions
- **Domain-Driven Design**: Clear separation between domain, application, and infrastructure layers
- **Immutable Data Structures**: Using Java records for clean, immutable domain models
- **Dependency Injection**: Leveraging Spring Framework for flexible component wiring
- **Process Pipeline**: Fluent API for building and executing processing pipelines

## Architecture Overview

The project implements a hexagonal architecture (also known as ports and adapters architecture) with clear separation of concerns:

### Domain Layer
The core of the application, containing business logic, entities, and business rules:
- **Models**: Simple, immutable domain entities (Cart, Customer, Order) implemented as Java records
- **States**: State classes implementing the State pattern for each entity
- **Events**: Event classes representing state change triggers
- **Ports**: Interfaces (with "Port" suffix) defining the required capabilities from outside the domain

The domain layer is completely independent of external frameworks and infrastructure details, following the dependency inversion principle.

### Application Layer
Orchestrates the flow of data and coordinates domain operations:
- **Services**: Business services implementing use cases
- **Event Listeners**: Components listening for domain events
- **Event Handlers**: Components handling specific events based on current state
- **Event Dispatcher**: Routes events to appropriate handlers based on state
- **Process Pipeline**: Provides a fluent API for building and executing processing pipelines

### Infrastructure Layer
Provides implementations for the domain ports:
- **Adapters**: Implementations of domain ports (with "Adapter" suffix)
- **Repositories**: In-memory implementations of data storage

All dependencies flow inward toward the domain layer, ensuring that the domain remains isolated from external concerns.

## Workflows

The application demonstrates 9 key workflows:

### Customer Workflows
1. **Customer Creation**: Creating a new customer and associated cart
2. **Customer Suspension**: Suspending an existing customer

### Cart Workflows
3. **Cart Creation**: Creating a new cart for a customer
4. **Cart Activation**: Activating a cart for use
5. **Cart Closure**: Closing a cart when it's no longer needed

### Order Workflows
6. **Order Creation**: Creating a new order from a cart
7. **Order Payment**: Processing payment for an order
8. **Order Delivery**: Marking an order as delivered
9. **Order Cancellation**: Cancelling an existing order

## State Transitions

### Cart States
- **Created**: Initial state when a cart is created
- **Active**: State when a cart is being used by a customer
- **Ordered**: State when a cart has been converted to an order
- **Closed**: Final state when a cart is no longer needed

### Customer States
- **Created**: Initial state when a customer is registered
- **Activated**: State when a customer account is active
- **Suspended**: State when a customer account is temporarily disabled

### Order States
- **New**: Initial state when an order is created
- **Paid**: State when payment is received
- **Delivered**: State when order is delivered
- **Canceled**: State when order is canceled

## Technologies Used

- **Java 21**: Latest Java features including records and pattern matching (strictly Java 21 must be used, as Mockito's Byte Buddy dependency only supports up to Java 21)
- **Spring Framework 6.2.8**: Core, Context, and Beans modules for dependency injection
- **Lombok 1.18.38**: Reducing boilerplate code
- **JUnit Jupiter 5.10.1**: Testing framework
- **Mockito 5.10.0**: Mocking framework for testing
- **Jetbrains Annotations 24.1.0**: Annotations for better code analysis
- **Gradle 9.0.0**: Build automation

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 21 (strictly Java 21, as Mockito's Byte Buddy dependency only supports up to Java 21)
- Gradle 9.0.0

### Building the Project
```bash
./gradlew build
```

### Running the Application
```bash
./gradlew run
```

### Running Tests
```bash
./gradlew test
```

This will execute all unit tests in the project. The test results will be available in the `build/reports/tests/test` directory.

You can also run specific test classes:
```bash
./gradlew test --tests "de.burger.it.application.cart.service.CartServiceTest"
```

Or specific test methods:
```bash
./gradlew test --tests "de.burger.it.application.order.service.OrderServiceTest.testCreateNewOrder"
```

## Usage Examples

The `Main.java` file demonstrates basic usage of the system:

```java
// Create a customer
var customer = new Customer(UUID.randomUUID(), "John Doe", "john@example.com");
customerService.createNewCustomer(customer);

// Create a cart for the customer
cartService.create(customer);

// Get all carts for the customer
var carts = cartService.findAllCartByCustomer(customer);

// Activate a cart
cartService.activate(carts.getFirst(), customer);

// Suspend a customer
customerService.suspendCustomer(customer);
```

### Null Object Pattern Usage

The system uses the Null Object pattern to eliminate null checks:

```java
// Example from OrderService
public OrderLike createNewOrder(CartLike cart) {
    if (cart.isNull()) {
        return NullOrder.getInstance();
    }
    var order = new Order(UUID.randomUUID());
    eventPublisher.publishEvent(new OrderCreateEvent(order));
    return order;
}

// Using the returned OrderLike without worrying about nulls
OrderLike order = orderService.createNewOrder(cart);
// No need for null check - isNull() method can be used instead
if (!order.isNull()) {
    // Proceed with order operations
}
```

### Process Pipeline Usage

The system uses a fluent Process Pipeline API for handling events:

```java
// Example of building a processing pipeline
ProcessPipeline<CartCreateEvent> pipeline = new ProcessPipeline<CartCreateEvent>()
    .append(event -> {
        // First step: assign cart status
        cartStatusAssignmentPort.assignStatus(event.getCart(), CartStateType.CREATED);
        return event;
    })
    .append(event -> {
        // Second step: assign customer to cart
        cartCustomerAssignmentPort.assignCustomer(event.getCart(), event.getCustomer());
        return event;
    })
    .appendIf(
        event -> !event.getCustomer().isNull(), // Condition
        event -> {
            // Conditional step: save to repository only if customer is not null
            cartRepositoryPort.save(event.getCart());
            return event;
        }
    );

// Execute the pipeline
pipeline.execute(cartCreateEvent);
```

## Project Structure

```
src/
├── main/
│   └── java/
│       └── de/
│           └── burger/
│               └── it/
│                   ├── Main.java
│                   ├── application/
│                   │   ├── cart/
│                   │   │   ├── handler/
│                   │   │   ├── listener/
│                   │   │   └── service/
│                   │   ├── config/
│                   │   ├── customer/
│                   │   │   ├── handler/
│                   │   │   ├── listener/
│                   │   │   └── service/
│                   │   ├── event/
│                   │   ├── order/
│                   │   │   ├── handler/
│                   │   │   ├── listener/
│                   │   │   └── service/
│                   │   ├── process/
│                   │   └── state/
│                   ├── domain/
│                   │   ├── cart/
│                   │   │   ├── event/
│                   │   │   ├── model/
│                   │   │   ├── port/
│                   │   │   └── state/
│                   │   ├── common/
│                   │   │   ├── event/
│                   │   │   └── model/
│                   │   ├── customer/
│                   │   │   ├── event/
│                   │   │   ├── model/
│                   │   │   ├── port/
│                   │   │   └── state/
│                   │   ├── order/
│                   │   │   ├── event/
│                   │   │   ├── model/
│                   │   │   ├── port/
│                   │   │   └── state/
│                   │   └── relation/
│                   │       ├── model/
│                   │       └── port/
│                   └── infrastructure/
│                       ├── cart/
│                       │   ├── adapter/
│                       │   └── model/
│                       ├── customer/
│                       │   ├── adapter/
│                       │   ├── model/
│                       │   └── port/
│                       ├── order/
│                       │   └── adapter/
│                       └── relation/
│                           ├── adapter/
│                           └── model/
```

## Design Patterns Used

- **State Pattern**: For managing entity states and transitions (e.g., CartState, CustomerState, OrderState)
- **Observer Pattern**: For event notification and handling through the event-driven architecture
- **Dependency Injection**: For loose coupling between components using Spring Framework
- **Ports and Adapters Pattern**: Core of the hexagonal architecture, with ports defined in the domain and implemented by adapters in the infrastructure layer
- **Factory Method**: For creating state objects and handling state transitions
- **Null Object Pattern**: For eliminating null checks by providing special "null" implementations (CartNullObject, CustomerNullObject, OrderNullObject) that implement the domain interfaces (Cart, Customer, OrderLike)
- **Pipeline Pattern**: For building and executing processing pipelines with a fluent API

## New Implementations

The following implementations were recently added and integrated into the system:

- Relation Management
  - Domain
    - CartCustomerAssignment (model), CartOrderAssignment (model)
    - Ports: CartCustomerAssignmentPort, CartOrderAssignmentPort, CartOrderAssignmentRepositoryPort
  - Infrastructure
    - Adapters: CartCustomerAssignmentAdapter, CartOrderAssignmentAdapter
    - Repository: InMemoryCartOrderAssignmentRepository
  - Purpose: Manage relationships between carts/customers and carts/orders in memory, with thread-safe collections.

- Status Assignment Adapters
  - CartStatusAssignmentAdapter: Assigns and reads CartStateType for a Cart
  - CustomerStatusAssignmentAdapter: Assigns and reads CustomerStateType for a Customer
  - OrderStatusAssignmentAdapter: Assigns and reads OrderStateType for an Order

- Process Configuration (Spring Beans)
  - CartProcessConfig: Pipelines for CartCreateEvent, CartActiveEvent, CartCloseEvent
  - CustomerProcessConfig: Pipelines for CustomerCreateEvent and CustomerSuspendEvent
  - OrderProcessConfig: Pipelines for OrderCreateEvent, OrderPayEvent, OrderDeliverEvent, OrderCancelEvent
  - Each bean wires the corresponding handlers using the fluent ProcessPipeline API (append, appendIf, andThen).

- In-memory Repositories
  - InMemoryCartRepository, InMemoryCustomerRepository, InMemoryOrderRepository
  - Used by services to persist aggregates during tests/demo without external DB.

- Event Handlers (selection)
  - Cart: OnCartCreateAssignCartStatus, OnCartCreateAssignCustomer, OnCartCreateSaveRepository, OnCartActiveAssignCartStatus, OnCartCloseAssignCartStatus
  - Customer: OnCustomerCreateAssignActive, OnCustomerCreateSaveRepository, OnCustomerCreateNewCart, OnCustomerSuspendAssignSuspend, OnCustomerSuspendSaveRepository
  - Order: OnOrderCreateAssignNewState, OnOrderCreateSaveRepository, OnOrderPayAssignPaidState, OnOrderDeliverAssignDeliveredState, OnOrderCancelAssignCanceledState

## Code Quality and CI

- Qodana: Static analysis configured via qodana.yaml and GitHub Actions workflow .github/workflows/qodana_code_quality.yml
- Complexity Analysis: Lizard reports converted to SARIF using scripts/lizard_to_sarif.py (see build/complexity/lizard.json for sample output)
- How to run locally
  - Generate reports with Gradle build and tests: `./gradlew build test`
  - Qodana can be run via Docker or GitHub Actions; see qodana.yaml for profile and thresholds.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Thanks to all contributors who have helped shape this project
- Inspired by Domain-Driven Design principles and best practices