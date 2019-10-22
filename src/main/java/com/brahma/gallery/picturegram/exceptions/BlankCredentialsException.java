package com.brahma.gallery.picturegram.exceptions;

public class BlankCredentialsException extends  Exception {
    public BlankCredentialsException (String message){
        super(message);
    }

    public BlankCredentialsException (String message, Throwable cause){
        super(message, cause);
    }
}
