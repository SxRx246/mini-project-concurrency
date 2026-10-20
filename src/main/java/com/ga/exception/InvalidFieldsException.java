package com.ga.exception;

/**
 * Exception thrown when employee data contains invalid fields.
 */
public class InvalidFieldsException extends RuntimeException{
        public InvalidFieldsException(String message){
            super(message);
        }

}
