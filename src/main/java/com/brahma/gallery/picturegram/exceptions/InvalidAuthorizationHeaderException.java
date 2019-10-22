package com.brahma.gallery.picturegram.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException {
    public InvalidAuthorizationHeaderException (String message){
        super(message);
    }

    public InvalidAuthorizationHeaderException (String message, Throwable cause){
        super(message, cause);
    }
}
