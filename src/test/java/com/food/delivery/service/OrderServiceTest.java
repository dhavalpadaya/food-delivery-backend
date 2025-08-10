package com.food.delivery.service;

import com.food.delivery.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = OrderService.getOrderService();
    }

    private Restaurant createRestaurant() {
        return new Restaurant("R1", "TestRestaurant", new Location(19.0760, 72.8777));
    }

    private Customer createCustomer() {
        Customer customer = new Customer("C1", "Testy");
        customer.setCustLocation(new Location(19.1197, 72.8468));
        return customer;
    }

    @Test
    void testAddOrderGeneratesUniqueIdAndStoresOrder() {
        Order order1 = orderService.addOrder(createRestaurant(), createCustomer(), 15);
        Order order2 = orderService.addOrder(createRestaurant(), createCustomer(), 20);

        assertNotNull(order1);
        assertNotNull(order2);
        assertNotEquals(order1.getOrderId(), order2.getOrderId());

        assertEquals(2, orderService.getOrderMap().size());
        assertTrue(orderService.getOrderMap().containsKey(order1.getOrderId()));
        assertTrue(orderService.getOrderMap().containsKey(order2.getOrderId()));
    }

    @Test
    void testCompleteOrderMarksAsCompletedAndRemovesFromMap() {
        Order order = orderService.addOrder(createRestaurant(), createCustomer(), 10);
        String orderId = order.getOrderId();

        Optional<Order> completed = orderService.completeOrder(orderId);

        assertTrue(completed.isPresent());
        assertEquals(OrderStatus.COMPLETED, completed.get().getOrderStatus());
        assertFalse(orderService.getOrderMap().containsKey(orderId));
    }

    @Test
    void testCompleteOrderWithInvalidIdReturnsEmpty() {
        Optional<Order> result = orderService.completeOrder("invalid-id");
        assertFalse(result.isPresent());
    }

    @Test
    void testRemoveOrderDeletesOrderFromMap() {
        Order order = orderService.addOrder(createRestaurant(), createCustomer(), 12);
        String orderId = order.getOrderId();

        Optional<Order> removed = orderService.removeOrder(orderId);

        assertTrue(removed.isPresent());
        assertEquals(orderId, removed.get().getOrderId());
        assertFalse(orderService.getOrderMap().containsKey(orderId));
    }

    @Test
    void testRemoveOrderWithInvalidIdReturnsEmpty() {
        Optional<Order> result = orderService.removeOrder("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    void testGetOrderReturnsCorrectOrder() {
        Order order = orderService.addOrder(createRestaurant(), createCustomer(), 8);
        String orderId = order.getOrderId();

        Optional<Order> fetched = orderService.getOrder(orderId);

        assertTrue(fetched.isPresent());
        assertEquals(orderId, fetched.get().getOrderId());
    }

    @Test
    void testGetOrderWithInvalidIdReturnsEmpty() {
        Optional<Order> result = orderService.getOrder("fake-id");
        assertFalse(result.isPresent());
    }

    @Test
    void testSingletonInstanceIsConsistent() {
        OrderService instance1 = OrderService.getOrderService();
        OrderService instance2 = OrderService.getOrderService();

        assertSame(instance1, instance2);
    }
}