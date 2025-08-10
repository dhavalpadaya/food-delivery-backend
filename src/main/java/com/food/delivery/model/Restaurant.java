package com.food.delivery.model;

public class Restaurant {
    private final String restId;
    private String restName;
    private Location restLocation;

    public Restaurant(String restId, String restName, Location restLocation) {
        this.restId = restId;
        this.restName = restName;
        this.restLocation = restLocation;
    }

    public String getRestId() {
        return restId;
    }

    public String getRestName() {
        return restName;
    }

    public Location getRestLocation() {
        return restLocation;
    }

    @Override
    public String toString() {
        return "[Restaurant] " + this.restName + " " + this.restLocation;
    }
}
