package com.food.delivery.service;

import com.food.delivery.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final Map<String, Order> orderMap;
    private static final OrderService orderServiceObj = new OrderService();
    private final AtomicInteger atomicInteger;

    private OrderService(){
        this.orderMap = new HashMap<>();
        this.atomicInteger = new AtomicInteger(1);
        logger.info("OrderService initialized");
    }

    public static OrderService getOrderService(){
        logger.debug("getOrderService() called");
        return orderServiceObj;
    }

    public Order addOrder(Restaurant pickUpRestaurant, Customer deliverCustomer,
                          double prepTimeMinutes){
        String orderId = getOrderId();
        Order order = new Order(orderId, pickUpRestaurant, deliverCustomer, prepTimeMinutes);
        orderMap.put(orderId, order);

        logger.info("Order added: id={}, restaurant={}, customer={}, prepTime={}min",
                orderId,
                pickUpRestaurant.getRestName(),
                deliverCustomer.getCustName(),
                prepTimeMinutes);
        return order;
    }

    public Optional<Order> completeOrder(String orderId){
        logger.info("completeOrder() called for orderId={}", orderId);

        if(orderMap.containsKey(orderId)){
            Order order = orderMap.remove(orderId);
            order.setOrderStatus(OrderStatus.COMPLETED);
            logger.info("Order {} marked as COMPLETED", orderId);
            return Optional.of(order);
        }
        logger.warn("completeOrder(): no order found for id={}", orderId);
        return Optional.empty();
    }

    public Optional<Order> removeOrder(String orderId){
        logger.info("removeOrder() called for orderId={}", orderId);

        if(orderMap.containsKey(orderId)){
            Order removedOrder = orderMap.remove(orderId);
            logger.info("Order {} removed", orderId);
            return Optional.of(removedOrder);
        }
        logger.warn("removeOrder(): no order found for id={}", orderId);
        return Optional.empty();
    }

    public  Optional<Order> getOrder(String orderId){
        logger.debug("getOrder() called for orderId={}", orderId);

        if(orderMap.containsKey(orderId)){
            logger.debug("Order {} found", orderId);
            return Optional.of(orderMap.get(orderId));
        }

        logger.warn("getOrder(): no order found for id={}", orderId);
        return Optional.empty();
    }

    public Map<String, Order> getOrderMap() {
        return orderMap;
    }

    private String getOrderId(){
        String id = IdPrefix.ORDER.get() + atomicInteger.getAndIncrement();
        logger.debug("Generated new Order ID: {}", id);
        return id;
    }
}
