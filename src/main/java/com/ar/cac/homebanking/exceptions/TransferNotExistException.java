package com.ar.cac.homebanking.exceptions;

public class TransferNotExistException extends RuntimeException{
    public TransferNotExistException(String message){
        super(message);
    }
}
