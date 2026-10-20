package com.ga.exception;

/**
 * Exception thrown when a requested employee cannot be found.
 */
public class AttributeNotFoundException extends RuntimeException{

    public AttributeNotFoundException(String message){
        super(message);
    }

}

