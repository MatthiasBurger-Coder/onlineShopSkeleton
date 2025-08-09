package de.burger.it;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.application.config.AppConfig;
import de.burger.it.application.customer.service.CustomerService;
import de.burger.it.application.order.service.OrderService;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.order.model.Order;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Initialize Spring context
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // Get services
        var customerService = context.getBean(CustomerService.class);
        var cartService = context.getBean(CartService.class);
        var orderService = context.getBean(OrderService.class);
        
        System.out.println("==========================================================");
        System.out.println("                CUSTOMER WORKFLOWS                        ");
        System.out.println("==========================================================");
        
        // 1. Customer Creation Workflow
        System.out.println("\n--- 1. CUSTOMER CREATION WORKFLOW ---");
        var customer1 = runCustomerCreationWorkflow(customerService, cartService);
        
        // 2. Customer Suspension Workflow
        System.out.println("\n--- 2. CUSTOMER SUSPENSION WORKFLOW ---");
        runCustomerSuspensionWorkflow(customerService, cartService, customer1);
        
        System.out.println("\n==========================================================");
        System.out.println("                  CART WORKFLOWS                          ");
        System.out.println("==========================================================");
        
        // 3. Cart Creation Workflow
        System.out.println("\n--- 3. CART CREATION WORKFLOW ---");
        var cart = runCartCreationWorkflow(customerService, cartService);
        
        // 4. Cart Activation Workflow
        System.out.println("\n--- 4. CART ACTIVATION WORKFLOW ---");
        runCartActivationWorkflow(customerService, cartService, cart);
        
        // 5. Cart Closure Workflow
        System.out.println("\n--- 5. CART CLOSURE WORKFLOW ---");
        runCartClosureWorkflow(customerService, cartService, cart);
        
        System.out.println("\n==========================================================");
        System.out.println("                  ORDER WORKFLOWS                         ");
        System.out.println("==========================================================");
        
        // 6. OrderDefault Creation Workflow
        System.out.println("\n--- 6. ORDER CREATION WORKFLOW ---");
        var order = runOrderCreationWorkflow(customerService, cartService, orderService);
        
        // 7. OrderDefault Payment Workflow
        System.out.println("\n--- 7. ORDER PAYMENT WORKFLOW ---");
        runOrderPaymentWorkflow(orderService, order);
        
        // 8. OrderDefault Delivery Workflow
        System.out.println("\n--- 8. ORDER DELIVERY WORKFLOW ---");
        runOrderDeliveryWorkflow(orderService, order);
        
        // 9. OrderDefault Cancellation Workflow
        System.out.println("\n--- 9. ORDER CANCELLATION WORKFLOW ---");
        var orderToCancel = runOrderCancellationWorkflow(customerService, cartService, orderService);
    }
    
    /**
     * Demonstrates the customer creation workflow
     */
    private static Customer runCustomerCreationWorkflow(CustomerService customerService, CartService cartService) {
        // Create a new customer
        var customer = new CustomerDefault(UUID.randomUUID(), "Matthias", "customer@example.com");
        customerService.createNewCustomer(customer);
        var customerState = customerService.getState(customer);
        
        // Display customer information
        System.out.println("Created customer: " + customer);
        System.out.println("Customer state: " + customerState);
        
        // Display customer's carts
        var carts = cartService.findAllCartByCustomer(customer);
        var cartStates = carts.stream().map(cartService::getState).toList();
        System.out.println("Customer carts: " + carts);
        System.out.println("Cart states: " + cartStates);
        
        return customer;
    }
    
    /**
     * Demonstrates the customer suspension workflow
     */
    private static void runCustomerSuspensionWorkflow(CustomerService customerService, CartService cartService, Customer customer) {
        // Suspend the customer
        customerService.suspendCustomer(customer);
        var customerState = customerService.getState(customer);
        
        // Display customer information after suspension
        System.out.println("Suspended customer: " + customer);
        System.out.println("Customer state after suspension: " + customerState);
        
        // Display customer's carts after suspension
        var carts = cartService.findAllCartByCustomer(customer);
        var cartStates = carts.stream().map(cartService::getState).toList();
        System.out.println("Customer carts after suspension: " + carts);
        System.out.println("Cart states after suspension: " + cartStates);
    }
    
    /**
     * Demonstrates the cart creation workflow
     */
    private static Cart runCartCreationWorkflow(CustomerService customerService, CartService cartService) {
        // Create a new customer for this workflow
        var customer = new CustomerDefault(UUID.randomUUID(), "John", "john@example.com");
        customerService.createNewCustomer(customer);
        
        // Create a cart for the customer
        cartService.create(customer);
        
        // Get the customer's carts
        var carts = cartService.findAllCartByCustomer(customer);
        var cart = carts.getFirst();
        var cartState = cartService.getState(cart);
        
        // Display cart information
        System.out.println("Created cart: " + cart);
        System.out.println("Cart state: " + cartState);
        System.out.println("For customer: " + customer);
        
        return cart;
    }
    
    /**
     * Demonstrates the cart activation workflow
     */
    private static void runCartActivationWorkflow(CustomerService customerService, CartService cartService, Cart cart) {
        // Get the customer for this cart
        var customer = new CustomerDefault(UUID.randomUUID(), "Jane", "jane@example.com");
        customerService.createNewCustomer(customer);
        
        // Create a cart for the customer
        cartService.create(customer);
        var carts = cartService.findAllCartByCustomer(customer);
        var cartToActivate = carts.getFirst();
        
        // Display cart state before activation
        System.out.println("Cart before activation: " + cartToActivate);
        System.out.println("Cart state before activation: " + cartService.getState(cartToActivate));
        
        // Activate the cart
        cartService.activate(cartToActivate, customer);
        
        // Display cart state after activation
        System.out.println("Cart after activation: " + cartToActivate);
        System.out.println("Cart state after activation: " + cartService.getState(cartToActivate));
    }
    
    /**
     * Demonstrates the cart closure workflow
     */
    private static void runCartClosureWorkflow(CustomerService customerService, CartService cartService, Cart cart) {
        // Get the customer for this cart
        var customer = new CustomerDefault(UUID.randomUUID(), "Bob", "bob@example.com");
        customerService.createNewCustomer(customer);
        
        // Create a cart for the customer
        cartService.create(customer);
        var carts = cartService.findAllCartByCustomer(customer);
        var cartToClose = carts.getFirst();
        
        // Display cart state before closure
        System.out.println("Cart before closure: " + cartToClose);
        System.out.println("Cart state before closure: " + cartService.getState(cartToClose));
        
        // Close the cart
        cartService.close(cartToClose, customer);
        
        // Display cart state after closure
        System.out.println("Cart after closure: " + cartToClose);
        System.out.println("Cart state after closure: " + cartService.getState(cartToClose));
    }
    
    /**
     * Demonstrates the order creation workflow
     */
    private static Order runOrderCreationWorkflow(CustomerService customerService, CartService cartService, OrderService orderService) {
        // Create a new customer for this workflow
        var customer = new CustomerDefault(UUID.randomUUID(), "Alice", "alice@example.com");
        customerService.createNewCustomer(customer);
        
        // Create a cart for the customer
        cartService.create(customer);
        var carts = cartService.findAllCartByCustomer(customer);
        var cart = carts.getFirst();
        
        // Activate the cart
        cartService.activate(cart, customer);
        
        // Create an order from the cart
        var order = orderService.createNewOrder(cart);
        
        // Display order information
        System.out.println("Created order: " + order);
        System.out.println("From cart: " + cart);
        System.out.println("For customer: " + customer);
        
        return order;
    }
    
    /**
     * Demonstrates the order payment workflow
     */
    private static void runOrderPaymentWorkflow(OrderService orderService, Order order) {
        // Display order before payment
        System.out.println("OrderDefault before payment: " + order);
        
        // Pay the order
        orderService.payOrder(order);
        
        // Display order after payment
        System.out.println("OrderDefault after payment: " + order);
    }
    
    /**
     * Demonstrates the order delivery workflow
     */
    private static void runOrderDeliveryWorkflow(OrderService orderService, Order order) {
        // Display order before delivery
        System.out.println("OrderDefault before delivery: " + order);
        
        // Deliver the order
        orderService.deliverOrder(order);
        
        // Display order after delivery
        System.out.println("OrderDefault after delivery: " + order);
    }
    
    /**
     * Demonstrates the order cancellation workflow
     */
    private static Order runOrderCancellationWorkflow(CustomerService customerService, CartService cartService, OrderService orderService) {
        // Create a new customer for this workflow
        var customer = new CustomerDefault(UUID.randomUUID(), "Charlie", "charlie@example.com");
        customerService.createNewCustomer(customer);
        
        // Create a cart for the customer
        cartService.create(customer);
        var carts = cartService.findAllCartByCustomer(customer);
        var cart = carts.getFirst();
        
        // Activate the cart
        cartService.activate(cart, customer);
        
        // Create an order from the cart
        var order = orderService.createNewOrder(cart);
        
        // Display order before cancellation
        System.out.println("OrderDefault before cancellation: " + order);
        
        // Cancel the order
        orderService.cancelOrder(order);
        
        // Display order after cancellation
        System.out.println("OrderDefault after cancellation: " + order);
        
        return order;
    }
}