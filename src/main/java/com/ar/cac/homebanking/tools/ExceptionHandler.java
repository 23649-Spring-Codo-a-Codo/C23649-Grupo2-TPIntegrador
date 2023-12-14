package com.ar.cac.homebanking.tools;

import com.ar.cac.homebanking.exceptions.InsufficientFoundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class ExceptionHandler {

    public static ResponseEntity<?> handleException(Exception e, String errorMessage) {
        if (e instanceof InsufficientFoundsException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error en la operacion: " + errorMessage);
        } else if (e instanceof HttpMessageNotReadableException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en formato: " + errorMessage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + errorMessage);
        }
    }
}

