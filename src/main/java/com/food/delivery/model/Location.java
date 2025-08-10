package com.food.delivery.model;

import static com.food.delivery.constants.Constants.EARTH_RADIUS_KM;

public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Calculates great-circle distance to another location using the haversine formula.
     * @param other geo-coordinate
     * @return distance in kilometers
     */
    public double distanceTo(Location other) {
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2)
                * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public String toString() {
        return "[" + latitude + "," + longitude + "]";
    }
}
