package com.food.delivery.service;

import com.food.delivery.model.Location;
import com.food.delivery.model.Order;
import com.food.delivery.model.Route;
import com.food.delivery.model.Task;

import java.util.List;
import java.util.Map;

public interface DeliveryRouteCalculatorStrategy {
    Route findRoute(Map<String, Order> orderMap, Location startLocation, double speed);
}
