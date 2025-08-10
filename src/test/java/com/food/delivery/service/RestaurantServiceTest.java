package com.food.delivery.service;

import com.food.delivery.model.Location;
import com.food.delivery.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest {

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantService = RestaurantService.getRestaurantService();
    }

    private Location createLocation() {
        return new Location(19.0760, 72.8777); // Mumbai
    }

    @Test
    void testAddRestaurantGeneratesUniqueIdAndStoresIt() {
        Restaurant r1 = restaurantService.addRestaurant("Biryani House", createLocation());
        Restaurant r2 = restaurantService.addRestaurant("Tandoori Town", createLocation());

        assertNotNull(r1);
        assertNotNull(r2);
        assertNotEquals(r1.getRestId(), r2.getRestId());

        assertEquals("Biryani House", r1.getRestName());
        assertEquals("Tandoori Town", r2.getRestName());
    }

    @Test
    void testRemoveRestaurantDeletesFromMap() {
        Restaurant restaurant = restaurantService.addRestaurant("Sushi Central", createLocation());
        String restId = restaurant.getRestId();

        Optional<Restaurant> removed = restaurantService.removeRestaurant(restId);

        assertTrue(removed.isPresent());
        assertEquals(restId, removed.get().getRestId());
    }

    @Test
    void testRemoveRestaurantWithInvalidIdReturnsEmpty() {
        Optional<Restaurant> result = restaurantService.removeRestaurant("invalid-id");
        assertFalse(result.isPresent());
    }

    @Test
    void testSingletonInstanceIsConsistent() {
        RestaurantService instance1 = RestaurantService.getRestaurantService();
        RestaurantService instance2 = RestaurantService.getRestaurantService();

        assertSame(instance1, instance2);
    }
}