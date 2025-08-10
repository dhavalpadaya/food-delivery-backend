package com.food.delivery.model;

public class Order {
    private String orderId;
    private Restaurant pickupRestaurant;
    private Customer deliverCustomer;
    private double prepTimeMinutes;
    private OrderStatus orderStatus;

    public Order(String orderId, Restaurant pickupRestaurant, Customer deliverCustomer, double prepTimeMinutes) {
        this.orderId = orderId;
        this.pickupRestaurant = pickupRestaurant;
        this.deliverCustomer = deliverCustomer;
        this.prepTimeMinutes = prepTimeMinutes;
        this.orderStatus = OrderStatus.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public Restaurant getPickupRestaurant() {
        return pickupRestaurant;
    }

    public Customer getDeliverCustomer() {
        return deliverCustomer;
    }

    public double getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "[Order] " + this.pickupRestaurant.getRestName() + " => " + this.deliverCustomer.getCustName()
                + " [Order Status - " + this.orderStatus.toString() + " ]"
                + " [Preparation Time - " + this.prepTimeMinutes + " Minutes ]";
    }
}
