# State Management System

A sophisticated state management system for e-commerce applications, demonstrating advanced design patterns and event-driven architecture. This project showcases how to implement complex state transitions for shopping carts, customers, and orders using domain-driven design principles.

**Version:** 2.0-SNAPSHOT

### What’s new (structure & build-logic)
- Consolidated Gradle 9 + Java 21 toolchain and CI (GitHub Actions uses Java 21).
- Introduced a composite build under `build-logic/` with convention plugins to keep `build.gradle.kts` lean.
- New logging conventions via `de.burger.it.build.infrastructure.logging.logging-conventions` and the aggregate `de.burger.it.build.application.logging-app` for unified SLF4J + Log4j2 setup.
- Clear layering of domain platform conventions, infrastructure conventions (Spring, Lombok, JetBrains annotations, Logging), and application aggregates.

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

- Java 21: Strictly Java 21 toolchain; code uses records and pattern matching. Mockito/Byte Buddy stack is validated on Java 21 in this project.
- Spring Framework 6.2.8: Core, Context, Beans (plus Spring Test for testing support).
- Lombok 1.18.38: Reduces boilerplate (compileOnly + annotationProcessor via convention plugin).
- JetBrains Annotations 24.1.0: Static analysis annotations.
- JUnit Jupiter 5.13.4: Primary testing framework on JUnit Platform 1.13.4.
- Spock 2.3 for Groovy 4.0 (Groovy BOM 4.0.22): BDD tests alongside JUnit; HTML reports via spock-reports 2.5.1.
- Mockito 5.18.0 and Hamcrest 3.0: Mocking and fluent assertions.
- SLF4J 2.0.17 + Log4j2 2.25.1 (via convention plugin): Unified logging stack with bridges (JUL/JCL) and optional async logging (LMAX Disruptor 3.4.4). AspectJ 1.9.24 runtime/weaver included for AOP support.
- Gradle 9.x (wrapper): Build automation; composite build with build-logic convention plugins.
- JaCoCo 0.8.12: Coverage reporting and verification (min 86% line coverage enforced).
- PIT Mutation Testing 1.20.1: Mutation testing via Gradle plugin; threshold 80%.
- Qodana 2025.1: Static analysis locally (qodana.yaml) and in CI (GitHub Actions).

## Gradle Plugin System (build-logic)

This project uses an embedded Gradle convention plugins system (composite build). It keeps build scripts short and reusable.

- Wiring: In `settings.gradle.kts`, the build logic is included as a composite build:
  - `includeBuild("build-logic")`
- Plugin location: `build-logic\src\main\kotlin\de\burger\it\build\...`
  - The package/path defines the plugin ID. Example:
    - File: `build-logic/src/main/kotlin/de/burger/it/build/application/spring-app.gradle.kts`
    - Plugin ID: `de.burger.it.build.application.spring-app`
- Applying plugins in this project (see `build.gradle.kts`):
  - `id("de.burger.it.build.application.spring-app")`
  - `id("de.burger.it.build.application.lombok-app")`
  - `id("de.burger.it.build.application.logging-app")` ← new
  - `id("de.burger.it.build.application.jetbrains-annotations-app")`
  - `id("de.burger.it.build.infrastructure.spring.spring-test-conventions")`
- Layering of conventions:
  - Domain/platform base: `de.burger.it.build.domain.platform-conventions` (toolchain, encoding, release target)
  - Infrastructure: e.g., `...spring.spring-core-conventions`, `...spring.spring-test-conventions`, `...lombok.lombok-conventions`, `...jetbrains.jetbrains-annotations-conventions`, `...logging.logging-conventions` ← new
  - App aggregates: e.g., `de.burger.it.build.application.spring-app`, `de.burger.it.build.application.logging-app` ← new, which bundle infrastructure conventions
- Versions and libraries are managed centrally via the Version Catalog: `gradle\libs.versions.toml`
  - Accessed inside plugins via `VersionCatalogsExtension` (e.g., `libs.findBundle("spring")`)
- How to add a new convention plugin (quick recipe):
  1. Create a new file at `build-logic/src/main/kotlin/de/burger/it/build/<scope>/<name>.gradle.kts`
  2. Set the `package` declaration to match the directory structure
  3. Declare required plugins/dependencies (via the `libs` catalog)
  4. Apply it in the project with `id("de.burger.it.build.<scope>.<name>")`

### Logging conventions (new)

The logging stack is provided via a reusable convention plugin to ensure a single SLF4J binding and consistent Log4j2 usage across modules:
- Infrastructure plugin: `de.burger.it.build.infrastructure.logging.logging-conventions`
  - Exposes slf4j-api (api), adds log4j-api/core and log4j-slf4j2-impl at runtime
  - Bridges for JUL and JCL are included, and conflicting bindings (logback, log4j-to-slf4j) are excluded globally
  - Aligns Log4j versions with the Log4j BOM and supports async logging via `disruptor`
  - Adds AspectJ runtime (and weaver at runtime) to support AOP-based logging
- Application aggregate: `de.burger.it.build.application.logging-app`
  - Composes `platform-conventions` and `logging-conventions`

Usage in root build.gradle.kts (already applied):
```
plugins {
    id("de.burger.it.build.application.logging-app")
}
```

You can now use SLF4J with Log4j2 backend without manually declaring these dependencies; the convention plugin wires them for you.

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 21 (strictly Java 21, as Mockito's Byte Buddy dependency only supports up to Java 21)
- Gradle 9.0.0

### Building the Project
- Windows (PowerShell/CMD):
  ```powershell
  .\gradlew.bat build
  ```

- macOS/Linux:
  ```bash
  ./gradlew build
  ```

### Running the Application
This project does not apply the Gradle 'application' plugin. You can run the demo Main class in your IDE, or from the command line after building:

- Windows (PowerShell/CMD):
  1) .\gradlew.bat build
  2) java -cp build\classes\java\main;build\resources\main de.burger.it.Main

- macOS/Linux:
  1) ./gradlew build
  2) java -cp build/classes/java/main:build/resources/main de.burger.it.Main

### Running Tests
- Windows (PowerShell/CMD):
  ```powershell
  .\gradlew.bat test
  ```

- macOS/Linux:
  ```bash
  ./gradlew test
  ```

This will execute all unit tests in the project. The test results will be available in the `build/reports/tests/test` directory.

You can also run specific test classes:
- Windows (PowerShell/CMD):
  ```powershell
  .\gradlew.bat test --tests "de.burger.it.application.cart.service.CartServiceTest"
  ```
- macOS/Linux:
  ```bash
  ./gradlew test --tests "de.burger.it.application.cart.service.CartServiceTest"
  ```

Or specific test methods:
- Windows (PowerShell/CMD):
  ```powershell
  .\gradlew.bat test --tests "de.burger.it.application.order.service.OrderServiceTest.testCreateNewOrder"
  ```
- macOS/Linux:
  ```bash
  ./gradlew test --tests "de.burger.it.application.order.service.OrderServiceTest.testCreateNewOrder"
  ```

#### Running Spock Tests
- Spock specs live under `src/test/groovy` and use the `*Spec.groovy` suffix (e.g., `SanitySpec.groovy`). They are executed automatically when you run `gradlew test`.
- Run a specific Spock spec:
  - Windows (PowerShell/CMD): `./gradlew.bat test --tests "de.burger.it.SanitySpec"`
  - macOS/Linux: `./gradlew test --tests "de.burger.it.SanitySpec"`
- Run a specific Spock feature method (use the exact feature name in quotes; wildcards are supported):
  - Windows (PowerShell/CMD): `./gradlew.bat test --tests "de.burger.it.SanitySpec.*basic sanity check*"`
  - macOS/Linux: `./gradlew test --tests "de.burger.it.SanitySpec.*basic sanity check*"`

### Running PIT- Tests
- Windows (PowerShell/CMD):
  ```powershell
  .\gradlew.bat pitest
  ```

- macOS/Linux:
  ```bash
  ./gradlew pitest
  ```

This will execute PIT mutation testing across the configured target classes and tests. The HTML report will be available under `build\reports\pitest\<timestamp>\index.html` (for example, `build\reports\pitest\202508101624\index.html`). The build is configured to fail if the mutation score drops below 80%.

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

The system uses the Null Object pattern to eliminate null checks. The `CartNullObject` uses a zero UUID and implements equality so that it also compares equal to `NullCartState` in tests:

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
build-logic/
├── src/
│   └── main/
│       └── kotlin/
│           └── de/
│               └── burger/
│                   └── it/
│                       └── build/
│                           ├── domain/
│                           │   └── platform-conventions.gradle.kts
│                           ├── application/
│                           │   ├── spring-app.gradle.kts
│                           │   ├── lombok-app.gradle.kts
│                           │   ├── logging-app.gradle.kts
│                           │   └── jetbrains-annotations-app.gradle.kts
│                           └── infrastructure/
│                               ├── spring/
│                               │   ├── spring-core-conventions.gradle.kts
│                               │   └── spring-test-conventions.gradle.kts
│                               ├── lombok/
│                               │   └── lombok-conventions.gradle.kts
│                               ├── logging/
│                               │   └── logging-conventions.gradle.kts
│                               └── jetbrains/
│                                   └── jetbrains-annotations-conventions.gradle.kts

gradle/
└── wrapper/

src/
├── main/
│   └── java/
│       └── de/
│           └── burger/
│               └── it/
│                   ├── application/
│                   │   ├── config/
│                   │   ├── process/
│                   │   ├── cart/
│                   │   │   ├── handler/
│                   │   │   ├── listener/
│                   │   │   ├── process/
│                   │   │   └── service/
│                   │   ├── customer/
│                   │   │   ├── handler/
│                   │   │   ├── listener/
│                   │   │   ├── process/
│                   │   │   └── service/
│                   │   └── order/
│                   │       ├── handler/
│                   │       ├── listener/
│                   │       ├── process/
│                   │       └── service/
│                   ├── domain/
│                   │   ├── cart/
│                   │   │   ├── event/
│                   │   │   ├── model/
│                   │   │   ├── port/
│                   │   │   └── state/
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
│                       │   └── model/
│                       ├── order/
│                       │   ├── adapter/
│                       │   └── model/
│                       └── relation/
│                           ├── adapter/
│                           └── model/
└── test/
    ├── generated_tests/
    ├── java/
    │   └── de/
    │       └── burger/
    │           └── it/
    │               ├── application/
    │               │   ├── config/
    │               │   ├── process/
    │               │   ├── cart/
    │               │   │   ├── handler/
    │               │   │   ├── listener/
    │               │   │   ├── process/
    │               │   │   └── service/
    │               │   ├── customer/
    │               │   │   ├── handler/
    │               │   │   ├── listener/
    │               │   │   ├── process/
    │               │   │   └── service/
    │               │   └── order/
    │               │       ├── handler/
    │               │       ├── listener/
    │               │       ├── process/
    │               │       └── service/
    │               ├── domain/
    │               │   ├── cart/
    │               │   │   ├── event/
    │               │   │   ├── model/
    │               │   │   ├── port/
    │               │   │   └── state/
    │               │   ├── customer/
    │               │   │   ├── event/
    │               │   │   ├── model/
    │               │   │   ├── port/
    │               │   │   └── state/
    │               │   ├── order/
    │               │   │   ├── event/
    │               │   │   ├── model/
    │               │   │   ├── port/
    │               │   │   └── state/
    │               │   └── relation/
    │               │       └── model/
    │               └── infrastructure/
    │                   ├── cart/
    │                   │   ├── adapter/
    │                   │   └── model/
    │                   ├── customer/
    │                   │   ├── adapter/
    │                   │   └── model/
    │                   ├── order/
    │                   │   ├── adapter/
    │                   │   └── model/
    │                   └── relation/
    │                       ├── adapter/
    │                       └── model/
    └── groovy/
        └── de/
            └── burger/
                └── it/
                    └── application/
                        └── customer/
                            └── service/
```

## Design Patterns Used

- **State Pattern**: For managing entity states and transitions (e.g., CartState, CustomerState, OrderState)
- **Observer Pattern**: For event notification and handling through the event-driven architecture
- **Dependency Injection**: For loose coupling between components using Spring Framework
- **Ports and Adapters Pattern**: Core of the hexagonal architecture, with ports defined in the domain and implemented by adapters in the infrastructure layer
- **Factory Method**: For creating state objects and handling state transitions
- **Null Object Pattern**: For eliminating null checks by providing special "null" implementations (CartNullObject, CustomerNullObject, OrderNullObject) that implement the domain interfaces (Cart, Customer, OrderLike)
- **Pipeline Pattern**: For building and executing processing pipelines with a fluent API


## Code Quality and CI

- Qodana: Static analysis configured via qodana.yaml with the YAML profile at .qodana/profiles/qodana-config.yaml.
- Qodana Complexity: Cyclomatic and other complexity metrics are enforced via the YAML profile at .qodana/profiles/qodana-config.yaml.
  - Enabled inspections include: JavaCyclomaticComplexity, JavaNPathComplexityInspection, JavaMethodMetrics, JavaClassMetrics, JavaOverlyNestedBlock, and JavaOverlyComplexBooleanExpression.
  - Thresholds (e.g., method cyclomatic complexity m_limit=10) can be adjusted in .qodana/profiles/qodana-config.yaml to tune sensitivity.
  - Results are available locally in qodana.sarif.json. You can also integrate Qodana in CI if desired.

- Mutation Testing (PIT): Ensures test suite quality by killing mutations.
  - Version: PIT core 1.20.1 (configured via Gradle plugin info.solidsoft.pitest)
  - Run: Windows `.\u0067radlew.bat pitest`, macOS/Linux `./gradlew pitest`
  - Report: `build\reports\pitest\<timestamp>\index.html` (e.g., `build\reports\pitest\202508101624\index.html`)
  - Threshold: mutation score threshold set to 80%
  - Notes: JDK 21 `--add-opens` flags and `useClasspathFile` are configured to support Mockito/Byte Buddy and long classpaths on Windows.

- Test Coverage (JaCoCo): Enforces minimum coverage and produces reports.
  - Minimum line coverage: 86% (build fails if below)
  - Reports: HTML and XML (`build\reports\jacoco\test\jacocoTestReport.xml`)
  - Run: Windows `.\u0067radlew.bat test jacocoTestReport`, macOS/Linux `./gradlew test jacocoTestReport`

- Spock (BDD tests with Groovy): Behavior-driven tests written in Groovy using Spock 2 on JUnit Platform.
  - Versions: Groovy 4.0.22 (BOM), Spock `spock-core:2.3-groovy-4.0` (see build.gradle.kts)
  - Location: Specs live under `src\test\groovy` (e.g., `SanitySpec.groovy`)
  - Run: Included in the standard Gradle test task — Windows `.\u0067radlew.bat test`, macOS/Linux `./gradlew test`
  - Reports: Standard Gradle test report at `build\reports\tests\test\index.html`

- How to run locally
  - Generate reports with Gradle build and tests: `./gradlew build test`
  - Run Qodana locally with Docker (example): `docker run --rm -it -v "${PWD}":/data -v "${PWD}/.qodana":/data/.qodana jetbrains/qodana-jvm:2025.1 --config,qodana.yaml`
  - CI: GitHub Actions workflow .github/workflows/qodana_code_quality.yml runs tests, generates JaCoCo coverage, and executes Qodana using qodana.yaml.

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


## How to execute the Forensics BTMGen Gradle plugin

The project is already configured to apply the plugin with ID `de.burger.forensics.btmgen` in `build.gradle.kts`, and to resolve it from your local Maven repository via `settings.gradle.kts`.

Follow these steps to run it.

1) Publish the plugin to your local Maven cache (only needed once per change)
- In the plugin project (the one that declares `id("de.burger.forensics.btmgen")`), run:
  - Windows: `gradlew.bat publishToMavenLocal`
  - macOS/Linux: `./gradlew publishToMavenLocal`

2) Make sure this project resolves the plugin from mavenLocal
- Already set in `settings.gradle.kts`:
  - `pluginManagement { repositories { mavenLocal(); gradlePluginPortal(); mavenCentral() } }`
  - And a mapping to the marker module: `useModule("de.burger.forensics.btmgen:de.burger.forensics.btmgen.gradle.plugin:1.0.0")`

3) Verify the build sees the plugin and list available tasks
- From this project root, run:
  - `gradlew.bat --refresh-dependencies tasks --all`
- Then filter for anything the plugin contributes (examples):
  - `gradlew.bat tasks --all | findstr /i btm`
  - `gradlew.bat tasks --all | findstr /i byteman`
- If the plugin adds tasks, you will see them grouped (often under a group like "forensics" or similar).

4) Execute the plugin task(s)
- Once you know the task name, run it directly. Examples (the exact name depends on the plugin implementation):
  - `gradlew.bat btmGen`
  - `gradlew.bat generateBytemanRules`
  - `gradlew.bat btmGenMain` (if the plugin creates per-source-set tasks)
- Add `-i` for info logs or `-d` for debug logs to see detailed output:
  - `gradlew.bat btmGen -i`

5) If you don’t see any new tasks
- Some plugins act as convention plugins and perform their work during existing lifecycle tasks.
- Try running a regular build with info logs and look for plugin messages:
  - `gradlew.bat clean build -i`
- Also make sure Gradle isn’t reusing a stale configuration-cache entry:
  - `gradlew.bat --no-configuration-cache tasks --all`
- If you just published a new plugin version locally, refresh dependencies:
  - `gradlew.bat --refresh-dependencies tasks`

6) Troubleshooting checklist
- Confirm the plugin is applied in `build.gradle.kts`:
  - `plugins { id("de.burger.forensics.btmgen") }`
- Confirm the IDs and coordinates match the published plugin:
  - Plugin ID: `de.burger.forensics.btmgen`
  - Marker module used in settings: `de.burger.forensics.btmgen:de.burger.forensics.btmgen.gradle.plugin:1.0.0`
- Ensure Java/Gradle versions match what the plugin was built for (Java 21, Gradle 9.x in this repo).
- Run with `--stacktrace -i` if a task fails, and check logs for classes or extensions the plugin registers.

7) Where are the outputs?
- If the plugin generates files (e.g., Byteman .btm rule files), they are typically placed under this project’s `build/` directory (for example, `build/btmgen` or a similar folder), unless the plugin exposes configuration to change the output. Inspect the Gradle logs to find the exact output directory.

If you’d like, share the exact task names the plugin registers (if any) and I can wire a convenience aggregate task (e.g., `generateTracingRules`) in this build that depends on them.




## Forensics Tracing BTM Generator — Setup and Usage

This project applies the Gradle plugin de.burger.forensics.btmgen, which analyzes Kotlin (and optionally simple Java) sources and generates Byteman rules to obtain forensic traces of call-chains, decisions, and selected variable writes.

Features (plugin)
- Generates AT ENTRY / AT EXIT rules for functions and methods to trace call-chains.
- Adds decision tracing for if/when/is expressions (true/false branches).
- Emits AFTER WRITE rules for configured local variables.
- Optional naive Java parser for additional coverage.
- Uses Kotlin PSI via kotlin-compiler-embeddable; no IDE required.

Prerequisites
- Gradle 7.0+ (this repo uses Gradle 9.x) and Kotlin DSL support
- Byteman JAR (e.g., byteman-download-<version>/lib/byteman.jar) for runtime injection
- A helper class implementation such as de.burger.forensics.ForensicsHelper providing methods invoked from rules (enter, exit, iff, writeVar, …)

Configuration in this project
- The plugin is applied in build.gradle.kts: id("de.burger.forensics.btmgen")
- Plugin is resolved from mavenLocal via settings.gradle.kts using the marker module mapping for version 1.0.0
- Defaults are configured via the forensicsBtmGen extension (Java sources, include Java parser, package prefix de.burger.it, helper FQN de.burger.forensics.ForensicsHelper)
- A concrete task generateBtmRules is registered to produce rules under build/forensics

Example configuration (already present in build.gradle.kts)
- Extension defaults:
  forensicsBtmGen {
      srcDirs.set(listOf("src/main/java"))
      pkgPrefix.set("")
      pkgPrefixes.set(listOf("de.burger.it"))
      helperFqn.set("de.burger.forensics.ForensicsHelper")
      entryExit.set(true)
      trackedVars.set(listOf("approved", "status"))
      includeJava.set(true)
  }
- Task to generate rules:
  tasks.register<de.burger.forensics.plugin.GenerateBtmTask>("generateBtmRules") { ... outputDir.set(layout.buildDirectory.dir("forensics")) }

How to generate Byteman rules
- Publish the plugin to your local Maven cache (only needed when you change the plugin):
  - Windows: gradlew.bat publishToMavenLocal
  - macOS/Linux: ./gradlew publishToMavenLocal
- From this project root, run:
  - Windows: gradlew.bat generateBtmRules
  - macOS/Linux: ./gradlew generateBtmRules
- Output: build/forensics/ with shard files such as tracing-0001-00001.btm

Load the generated rules with Byteman
1) Start your target JVM with the Byteman agent listening on a port (example: 9091):
- Linux/macOS:
  java -javaagent:/path/to/byteman.jar=listener:true,port:9091 -jar app.jar
- Windows:
  java -javaagent:C:\path\to\byteman.jar=listener:true,port:9091 -jar app.jar
2) Submit all generated .btm files:
- Linux/macOS:
  for f in build/forensics/*.btm; do
    bmsubmit -p 9091 -l "$f"
  done
- Windows cmd:
  for %f in (build\forensics\*.btm) do bmsubmit.bat -p 9091 -l "%f"
3) To unload later:
- bmsubmit -p 9091 -u

Generated rule highlights
- Call chain tracing: paired enter/exit helper calls for selected functions
- Decisions: iff, sw, and kase helper calls with preserved source line numbers
- Variable writes: AFTER WRITE rules for configured trackedVars

Example bootstrap snippet
RULE bootstrap@Forensics
CLASS *
METHOD *
HELPER de.burger.forensics.ForensicsHelper
AT ENTRY
IF true
DO startTrace("/var/log/forensics.log"), setQuota(5000), enableSampling(10)
ENDRULE

RULE shutdown@Forensics
CLASS *
METHOD *
HELPER de.burger.forensics.ForensicsHelper
AT EXIT
IF true
DO stopTrace()
ENDRULE

Kotlin specifics
- Top-level functions appear under synthetic <FileName>Kt classes
- Nested classes/objects use $ separators in JVM binary names
- Property writes use generated setters; trackedVars targets local variable assignments
- Inline/suspend functions are handled at PSI level; logical structure preserved

CI/CD note
- The plugin repository supports publishing to Plugin Portal and Maven Central via GitHub Actions. Provide credentials to enable automated releases on tags.

Troubleshooting
- Ensure the helper class FQN exists at runtime when loading rules into your JVM
- If tasks aren’t visible, refresh dependencies and disable configuration cache for a run:
  - gradlew.bat --refresh-dependencies --no-configuration-cache tasks --all
- Run with --stacktrace -i for more diagnostics
