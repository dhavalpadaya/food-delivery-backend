package com.food.delivery.service;

import com.food.delivery.model.IdPrefix;
import com.food.delivery.model.Location;
import com.food.delivery.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RestaurantService {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private final Map<String, Restaurant> restaurantMap;
    private final AtomicInteger atomicInteger;

    private static final RestaurantService restaurantServiceObj = new RestaurantService();

    private RestaurantService(){
        this.restaurantMap = new HashMap<>();
        this.atomicInteger = new AtomicInteger(1);
        logger.info("RestaurantService instantiated");
    }

    public static RestaurantService getRestaurantService(){
        logger.debug("getRestaurantService() called");
        return restaurantServiceObj;
    }

    public Restaurant addRestaurant(String restName, Location restLocation){
        String restId = getRestId();
        Restaurant restaurant = new Restaurant(restId,
                restName, restLocation);
        restaurantMap.put(restId, restaurant);
        logger.info("Added Restaurant: id={}, name={}, location={}",
                restId, restName, restLocation);
        return restaurant;
    }

    public Optional<Restaurant> removeRestaurant(String restId){
        logger.info("removeRestaurant() called for id={}", restId);

        if(restaurantMap.containsKey(restId)){
            Restaurant restaurant = restaurantMap.remove(restId);
            logger.info("Removed Restaurant: id={}, name={}",
                    restId, restaurant.getRestName());
            return Optional.of(restaurant);
        }
        logger.warn("removeRestaurant(): no restaurant found for id={}", restId);
        return Optional.empty();
    }

    private String getRestId(){
        String id = IdPrefix.RESTAURANT.get() + atomicInteger.getAndIncrement();
        logger.debug("Generated new Restaurant ID: {}", id);
        return id;
    }
}
