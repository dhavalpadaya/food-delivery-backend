package com.food.delivery.exception;

public class LocationOutsideAllowedAreaException extends RuntimeException{

    public LocationOutsideAllowedAreaException(String message){
        super(message);
    }
}
