// src/main/java/com/matthewcasperson/calendarapi/exceptions/AuthException.java

package com.matthewcasperson.calendarapi.exceptions;

public class AuthException extends RuntimeException {

    public AuthException(String message, Throwable cause){
        super(message, cause);
    }
}