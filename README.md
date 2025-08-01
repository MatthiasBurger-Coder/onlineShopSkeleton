# State Management System

A sophisticated state management system for e-commerce applications, demonstrating advanced design patterns and event-driven architecture. This project showcases how to implement complex state transitions for shopping carts, customers, and orders using domain-driven design principles.

## Features

- **State Management**: Robust state machine implementation for carts, customers, and orders
- **Event-Driven Architecture**: Event-based system for handling state transitions
- **Domain-Driven Design**: Clear separation between domain, application, and infrastructure layers
- **Immutable Data Structures**: Using Java records for clean, immutable domain models
- **Dependency Injection**: Leveraging Spring Framework for flexible component wiring

## Architecture Overview

The project follows a clean architecture approach with distinct layers:

### Domain Layer
Contains the core business logic, entities, and business rules:
- **Models**: Simple, immutable domain entities (Cart, Customer, Order)
- **States**: State classes implementing the State pattern for each entity
- **Events**: Event classes representing state change triggers
- **Ports**: Interfaces defining the required infrastructure capabilities

### Application Layer
Orchestrates the flow of data and coordinates domain operations:
- **Services**: Business services implementing use cases
- **Event Listeners**: Components listening for domain events
- **Event Handlers**: Components handling specific events based on current state
- **Event Dispatcher**: Routes events to appropriate handlers based on state

### Infrastructure Layer
Provides implementations for the domain ports:
- **Repositories**: In-memory implementations of data storage
- **Adapters**: Implementations of domain ports

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

- **Java 24**: Latest Java features including records and pattern matching
- **Spring Framework 6.2.8**: Core, Context, and Beans modules for dependency injection
- **Lombok 1.18.38**: Reducing boilerplate code
- **JUnit Jupiter 5.13.1**: Testing framework
- **Gradle 8.x**: Build automation

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 24 or later
- Gradle 8.x or later

### Building the Project
```bash
./gradlew build
```

### Running the Application
```bash
./gradlew run
```

## Usage Examples

The `Main.java` file demonstrates basic usage of the system:

```
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

## Project Structure

```
src/
├── main/
│   └── java/
│       └── de/
│           └── burger/
│               └── it/
│                   ├── Main.java
│                   ├── adapter/
│                   │   ├── cart/
│                   │   ├── customer/
│                   │   ├── order/
│                   │   └── relation/
│                   ├── application/
│                   │   ├── cart/
│                   │   ├── config/
│                   │   ├── customer/
│                   │   ├── dispatcher/
│                   │   ├── order/
│                   │   └── process/
│                   ├── domain/
│                   │   ├── cart/
│                   │   ├── customer/
│                   │   ├── order/
│                   │   └── relation/
│                   └── infrastructure/
│                       ├── cart/
│                       ├── customer/
│                       ├── order/
│                       └── relation/
```

## Design Patterns Used

- **State Pattern**: For managing entity states and transitions
- **Observer Pattern**: For event notification and handling
- **Dependency Injection**: For loose coupling between components
- **Adapter Pattern**: For adapting domain ports to infrastructure implementations
- **Factory Method**: For creating state objects

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