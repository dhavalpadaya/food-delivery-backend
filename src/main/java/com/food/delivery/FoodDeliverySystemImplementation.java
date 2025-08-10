package com.food.delivery;

import com.food.delivery.exception.EntityDeletionNotAllowedException;
import com.food.delivery.exception.LocationOutsideAllowedAreaException;
import com.food.delivery.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.food.delivery.constants.Constants.*;

public class FoodDeliverySystemImplementation {
    private static final Logger logger =
            LoggerFactory.getLogger(FoodDeliverySystemImplementation.class);

    public static void main(String[] args) {
        // Center of Mumbai, India
        // Latitude: 19.076090, Longitude: 72.877426
        logger.info("Center location (Mumbai): {}, {}", CENTER_LATITUDE, CENTER_LONGITUDE);

        Location validRestLocation1    = new Location(19.1092443, 72.711526);
        Location validRestLocation2    = new Location(19.098133, 72.866135);
        Location invalidRestLocation   = new Location(19.145898, 73.109780);

        Location validCustLocation1    = new Location(19.078423, 72.890054);
        Location validCustLocation2    = new Location(19.116705, 72.844735);
        Location invalidCustLocation   = new Location(18.884930, 73.149606);

        FoodDeliveryManager manager = new FoodDeliveryManager();

        logger.info("== 1) Add Customers ==");
        Customer karan = manager.addCustomer("Karan");
        logger.info("Registered customer: {}", karan);
        Customer arjun = manager.addCustomer("Arjun");
        logger.info("Registered customer: {}", arjun);

        logger.info("== 2) Add Restaurants ==");
        Restaurant r1 = manager.addRestaurant("TastyBites", validRestLocation1);
        logger.info("Added restaurant: {}", r1);
        Restaurant r2 = manager.addRestaurant("Taj", validRestLocation2);
        logger.info("Added restaurant: {}", r2);

        try {
            manager.addRestaurant("FarAwayGrill", invalidRestLocation);
        } catch (LocationOutsideAllowedAreaException ex) {
            logger.error("Failed to add FarAwayGrill: {}", ex.getMessage(), ex);
        }

        logger.info("== 3) Place Orders ==");
        Order order1 = manager.addOrder(r1, karan, validCustLocation1, 20);
        logger.info("Order placed successfully for Karan: {}", order1);
        Order order2 = manager.addOrder(r2, arjun, validCustLocation2, 10);
        logger.info("Order placed successfully for Arjun: {}", order2);

        logger.info("== 4) Remove Entities without completing Orders ==");
        try {
            manager.removeRestaurant(r1.getRestId());
        } catch (EntityDeletionNotAllowedException ex) {
            logger.error("Failed to remove restaurant {}: {}", r1.getRestId(), ex.getMessage(), ex);
        }

        try {
            manager.removeCustomer(karan.getCustId());
        } catch (EntityDeletionNotAllowedException ex) {
            logger.error("Failed to remove customer {}: {}", karan.getCustId(), ex.getMessage(), ex);
        }

        try {
            manager.removeOrder("ORDER_999");
        } catch (EntityDeletionNotAllowedException ex) {
            logger.error("Failed to remove order ORDER_999: {}", ex.getMessage(), ex);
        }

        try {
            Customer raj = manager.addCustomer("Raj");
            manager.addOrder(r1, raj, invalidCustLocation, 12.0);
        } catch (LocationOutsideAllowedAreaException ex) {
            logger.error("Failed to place order for Raj: {}", ex.getMessage(), ex);
        }

        logger.info("== 5) Compute Delivery Route ==");
        try {
            Location deliveryPartnerLocation = new Location(19.100485, 72.861901);
            double speed = 20;
            Route route = manager.findRoute(deliveryPartnerLocation, speed);
        } catch (LocationOutsideAllowedAreaException ex) {
            logger.error("Route computation aborted: {}", ex.getMessage(), ex);
        }

        logger.info("== 6) Complete Orders ==");
        manager.completeOrder(order1.getOrderId());
        logger.info("Completed order {}", order1.getOrderId());
        manager.completeOrder(order2.getOrderId());
        logger.info("Completed order {}", order2.getOrderId());

        logger.info("== 7) Remove Entities after completing Orders ==");
        manager.removeRestaurant(r1.getRestId());
        logger.info("Restaurant removed with id {}", r1.getRestId());
        manager.removeCustomer(karan.getCustId());
        logger.info("Customer removed with id {}", karan.getCustId());

        logger.info("=== Scenarios Completed ===");
    }
}