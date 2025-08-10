package com.food.delivery.service;

import com.food.delivery.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OptimalDeliveryRouteCalculatorStrategyTest {

    private final OptimalDeliveryRouteCalculatorStrategy strategy = new OptimalDeliveryRouteCalculatorStrategy();

    @Test
    void testSingleOrderRouteCalculation() {
        // Realistic coordinates: Mumbai, Bandra, Andheri
        Location start = new Location(19.0760, 72.8777); // Mumbai
        Location restLoc = new Location(19.0590, 72.8295); // Bandra
        Location custLoc = new Location(19.1197, 72.8468); // Andheri

        Restaurant restaurant = new Restaurant("R1", "Pizza Palace", restLoc);
        Customer customer = new Customer("C1", "Customer1");
        customer.setCustLocation(custLoc);

        Order order = new Order("O1", restaurant, customer, 10); // prep time = 10 min

        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put(order.getOrderId(), order);

        Route route = strategy.findRoute(orderMap, start, 30); // speed = 30 km/h

        assertNotNull(route);
        assertEquals(2, route.getTaskList().size());

        Task pickup = route.getTaskList().get(0);
        Task delivery = route.getTaskList().get(1);

        assertEquals("O1", pickup.getOrderId());
        assertEquals(TaskType.PICKUP, pickup.getTaskType());
        assertEquals(TaskType.DELIVERY, delivery.getTaskType());

        double distToPickup = start.distanceTo(restLoc);
        double travelTimeToPickUp = ((distToPickup/30)*60);
        double distToDelivery = restLoc.distanceTo(custLoc);
        double travelTimeToDelivery = (distToDelivery/30)*60;
        double expectedTotalTime = (travelTimeToPickUp + Math.max(10-travelTimeToPickUp,0)) + travelTimeToDelivery; // add prep time

        assertEquals(expectedTotalTime, route.getTotalTimeInMinutes(), 1.0); // allow 1 min tolerance
    }

    @Test
    void testMultipleOrdersOptimalSequence() {
        Location start = new Location(19.0760, 72.8777); // Mumbai

        Restaurant r1 = new Restaurant("R1", "Burger Hub", new Location(19.0580, 72.8300)); // Bandra
        Customer c1 = new Customer("C1", "Customer2");
        c1.setCustLocation(new Location(19.1180, 72.8470)); // Andheri
        Order o1 = new Order("O1", r1, c1, 5);

        Restaurant r2 = new Restaurant("R2", "Sushi Spot", new Location(19.0700, 72.8500)); // Dadar
        Customer c2 = new Customer("C2", "Customer3");
        c2.setCustLocation(new Location(19.1300, 72.8600)); // Jogeshwari
        Order o2 = new Order("O2", r2, c2, 5);

        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put(o1.getOrderId(), o1);
        orderMap.put(o2.getOrderId(), o2);

        Route route = strategy.findRoute(orderMap, start, 40); // speed = 40 km/h

        assertNotNull(route);
        assertEquals(4, route.getTaskList().size());

        // Ensure all pickups precede their respective deliveries
        Map<String, Boolean> pickedUp = new HashMap<>();
        for (Task task : route.getTaskList()) {
            if (task.getTaskType() == TaskType.PICKUP) {
                pickedUp.put(task.getOrderId(), true);
            } else {
                assertTrue(pickedUp.getOrDefault(task.getOrderId(), false),
                        "Delivery occurred before pickup for order " + task.getOrderId());
            }
        }
    }

    @Test
    void testEmptyOrderMapReturnsEmptyRoute() {
        Location start = new Location(19.0760, 72.8777); // Mumbai
        Map<String, Order> emptyMap = new HashMap<>();

        Route route = strategy.findRoute(emptyMap, start, 50); // speed = 50 km/h

        assertNotNull(route);
        assertEquals(0, route.getTaskList().size());
        assertEquals(0.0, route.getTotalTimeInMinutes(), 0.01);
    }
}