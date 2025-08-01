package de.burger.it;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.application.customer.service.CustomerService;
import de.burger.it.application.config.AppConfig;
import de.burger.it.domain.customer.model.Customer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        var customer1Service = context.getBean(CustomerService.class);
        var customer1 = new Customer(UUID.randomUUID(), "Matthias", "<EMAIL>");
        customer1Service.createNewCustomer(customer1);
        var customer1State = customer1Service.getState(customer1);

        var customer2Service = context.getBean(CustomerService.class);
        var customer2 = new Customer(UUID.randomUUID(), "Matthias", "<zweinstein>");
        customer2Service.createNewCustomer(customer2);
        var customer2State = customer2Service.getState(customer2);


        var cartService1 = context.getBean(CartService.class);
        cartService1.create(customer1);
        var cartService2 = context.getBean(CartService.class);

        var listOfAllCarts1 = cartService1.findAllCartByCustomer(customer1);
        var listOfAllCarts2 = cartService2.findAllCartByCustomer(customer2);

        var listOfAllCartStats1 = listOfAllCarts1.stream().map(cartService1::getState).toList();
        var listOfAllCartStats2 = listOfAllCarts2.stream().map(cartService2::getState).toList();

        System.out.println(customer1);
        System.out.println(customer1State);
        System.out.println(listOfAllCarts1);
        System.out.println(listOfAllCartStats1);
        System.out.println("-------------");

        System.out.println(customer2);
        System.out.println(customer2State);
        System.out.println(listOfAllCarts2);
        System.out.println(listOfAllCartStats2);
        System.out.println("-------------");

        customer1Service.suspendCustomer(customer1);
        customer1State = customer1Service.getState(customer1);
        listOfAllCarts1 = cartService1.findAllCartByCustomer(customer1);
        listOfAllCartStats1 = listOfAllCarts1.stream().map(cartService1::getState).toList();

        System.out.println(customer1);
        System.out.println(customer1State);
        System.out.println(listOfAllCarts1);
        System.out.println(listOfAllCartStats1);
        System.out.println("-------------");

        cartService1.activate(listOfAllCarts1.getFirst(), customer1);
        customer1State = customer1Service.getState(customer1);
        listOfAllCarts1 = cartService1.findAllCartByCustomer(customer1);
        listOfAllCartStats1 = listOfAllCarts1.stream().map(cartService1::getState).toList();

        System.out.println(customer1);
        System.out.println(customer1State);
        System.out.println(listOfAllCarts1);
        System.out.println(listOfAllCartStats1);

    }
}