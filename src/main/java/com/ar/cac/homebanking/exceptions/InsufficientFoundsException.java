package com.ar.cac.homebanking.exceptions;

public class InsufficientFoundsException extends RuntimeException{
    public InsufficientFoundsException(String message) {
        super(message);
    }
}
