package com.food.delivery;

import com.food.delivery.exception.EntityDeletionNotAllowedException;
import com.food.delivery.exception.EntityNotFoundException;
import com.food.delivery.exception.LocationOutsideAllowedAreaException;
import com.food.delivery.model.*;
import com.food.delivery.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.food.delivery.constants.Constants.*;

public class FoodDeliveryManager {
    private static final Logger logger = LoggerFactory.getLogger(FoodDeliveryManager.class);

    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final Location centerLocation;
    private final DeliveryRouteCalculatorStrategy deliveryRouteCalculatorStrategy;

    public FoodDeliveryManager() {
        this.restaurantService = RestaurantService.getRestaurantService();
        this.customerService = CustomerService.getCustomerService();
        this.orderService = OrderService.getOrderService();
        this.centerLocation = new Location(CENTER_LATITUDE, CENTER_LONGITUDE);
        this.deliveryRouteCalculatorStrategy = new OptimalDeliveryRouteCalculatorStrategy();

        logger.info("Initialized FoodDeliveryManager with center at {} and strategy {}",
                centerLocation, deliveryRouteCalculatorStrategy.getClass().getSimpleName());
    }

    public Order addOrder(Restaurant pickUpRestaurant,
                          Customer deliverCustomer,
                          Location deliverLocation,
                          double prepTimeMinutes) {
        logger.info("addOrder() called with restaurant={}, customer={}, location={}, prepTime={}",
                pickUpRestaurant.getRestId(),
                deliverCustomer.getCustId(),
                deliverLocation,
                prepTimeMinutes);

        if (!isValidLocation(deliverLocation)) {
            logger.warn("addOrder() failed: deliverLocation {} is outside allowed radius {}",
                    deliverLocation, ALLOWED_RADIUS_KM);
            throw new LocationOutsideAllowedAreaException(
                    "Order's deliverLocation is not in allowedRadius");
        }

        deliverCustomer.setCustLocation(deliverLocation);
        Order order = orderService.addOrder(pickUpRestaurant, deliverCustomer, prepTimeMinutes);
        logger.info("Order created successfully: orderId={}, status={}",
                order.getOrderId(),
                order.getOrderStatus());
        return order;
    }

    public Order completeOrder(String orderId){
        logger.info("completeOrder() called for orderId={}", orderId);
        Optional<Order> order = this.orderService.completeOrder(orderId);
        if(!order.isPresent()){
            logger.warn("completeOrder(): no order found for id {}", orderId);
            throw new EntityNotFoundException("Order is not available with given id " + orderId);
        }
        logger.info("Order {} completed successfully", orderId);
        return order.get();
    }

    public Optional<Order> removeOrder(String orderId) {
        logger.info("removeOrder() called for orderId={}", orderId);
        Optional<Order> removed = orderService.removeOrder(orderId);
        if (removed.isPresent()) {
            logger.info("Order {} removed successfully", orderId);
        } else {
            logger.warn("removeOrder(): no order found for id {}", orderId);
        }
        return removed;
    }

    public Route findRoute(Location startLocation, double speed) {
        logger.info("findRoute() called with location={}", startLocation);

        Route route = this.deliveryRouteCalculatorStrategy.findRoute(this.orderService.getOrderMap(), startLocation, speed);
        logger.debug("Route computed: {}", route);
        return route;
    }

    public Restaurant addRestaurant(String restName, Location restLocation) {
        logger.info("addRestaurant() called with name={} at location={}", restName, restLocation);

        if (!isValidLocation(restLocation)) {
            logger.warn("addRestaurant() failed: location {} outside allowed radius {}",
                    restLocation, ALLOWED_RADIUS_KM);
            throw new LocationOutsideAllowedAreaException(
                    "Restaurant's location is not in allowedRadius");
        }

        Restaurant r = restaurantService.addRestaurant(restName, restLocation);
        logger.info("Restaurant added: id={}, name={}", r.getRestId(), r.getRestName());
        return r;
    }

    public Optional<Restaurant> removeRestaurant(String restId) {
        logger.info("removeRestaurant() called for restId={}", restId);

        if (!listPendingOrdersForRestaurant(restId).isEmpty()) {
            logger.warn("removeRestaurant() aborted: restaurant {} has active orders", restId);
            throw new EntityDeletionNotAllowedException("Restaurant has active orders");
        }

        Optional<Restaurant> removed = restaurantService.removeRestaurant(restId);
        removed.ifPresent(r -> logger.info("Restaurant {} removed", restId));
        return removed;
    }

    public Customer addCustomer(String custName) {
        logger.info("addCustomer() called with name={}", custName);
        Customer c = customerService.addCustomer(custName);
        logger.info("Customer added: id={}, name={}", c.getCustId(), c.getCustName());
        return c;
    }

    public Optional<Customer> removeCustomer(String custId) {
        logger.info("removeCustomer() called for custId={}", custId);

        if (!listPendingOrdersOfCustomer(custId).isEmpty()) {
            logger.warn("removeCustomer() aborted: customer {} has active orders", custId);
            throw new EntityDeletionNotAllowedException("Customer has active orders");
        }

        Optional<Customer> removed = customerService.removeCustomer(custId);
        removed.ifPresent(c -> logger.info("Customer {} removed", custId));
        return removed;
    }

    private List<Order> listPendingOrdersForRestaurant(String restId) {
        return this.orderService.getOrderMap().values().stream()
                .filter(order -> OrderStatus.PENDING.equals(order.getOrderStatus()))
                .filter(order -> restId.equals(order.getPickupRestaurant().getRestId()))
                .collect(Collectors.toList());
    }

    private List<Order> listPendingOrdersOfCustomer(String custId) {
        return this.orderService.getOrderMap().values().stream()
                .filter(order -> OrderStatus.PENDING.equals(order.getOrderStatus()))
                .filter(order -> custId.equals(order.getDeliverCustomer().getCustId()))
                .collect(Collectors.toList());
    }

    /**
     * Validates if given location is within the allowed radius.
     * @return true if valid location
     */
    private boolean isValidLocation(Location location) {
        double distance = centerLocation.distanceTo(location);
        logger.debug("Checking location {}: distance={}km (allowed={}km)",
                location, distance, ALLOWED_RADIUS_KM);
        return distance <= ALLOWED_RADIUS_KM;
    }
}