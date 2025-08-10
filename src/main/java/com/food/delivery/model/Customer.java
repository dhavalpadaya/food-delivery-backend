package com.food.delivery.model;

public class Customer {
    private final String custId;
    private String custName;
    private Location custLocation;

    public Customer(String custId, String custName) {
        this.custId = custId;
        this.custName = custName;
    }

    public void setCustLocation(Location custLocation) {
        this.custLocation = custLocation;
    }

    public String getCustId() {
        return custId;
    }

    public String getCustName() {
        return custName;
    }

    public Location getCustLocation() {
        return custLocation;
    }

    @Override
    public String toString() {
        return "[Customer] " + this.custName + " " + (this.custLocation != null ? this.custLocation : "");
    }
}
