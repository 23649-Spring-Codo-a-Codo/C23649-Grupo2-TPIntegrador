package com.ar.cac.homebanking.exceptions;

public class TransferNotFoundException extends RuntimeException{
    public TransferNotFoundException(String message) {
        super(message);
    }
}
