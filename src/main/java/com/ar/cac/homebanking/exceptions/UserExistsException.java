package com.ar.cac.homebanking.exceptions;

public class UserExistsException extends RuntimeException  {
    public UserExistsException(String message) {
        super(message);
    }
}