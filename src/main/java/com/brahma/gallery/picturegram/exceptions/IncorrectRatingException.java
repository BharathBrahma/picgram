package com.brahma.gallery.picturegram.exceptions;

public class IncorrectRatingException extends RuntimeException {
    public IncorrectRatingException (String message){
        super(message);
    }

    public IncorrectRatingException (String message, Throwable cause){
        super(message, cause);
    }
}
