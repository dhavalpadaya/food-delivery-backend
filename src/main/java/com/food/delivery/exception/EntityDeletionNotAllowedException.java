package com.food.delivery.exception;

public class EntityDeletionNotAllowedException extends RuntimeException{

    public EntityDeletionNotAllowedException(String message){
        super(message);
    }
}
