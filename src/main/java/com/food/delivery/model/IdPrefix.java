package com.food.delivery.model;

public enum IdPrefix {
    CUSTOMER("CUST_"),
    RESTAURANT("REST_"),
    ORDER("ORDER_");

    private final String prefix;

    IdPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String get() {
        return prefix;
    }
}
