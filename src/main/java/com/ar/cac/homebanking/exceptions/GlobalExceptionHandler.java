package com.ar.cac.homebanking.exceptions;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.repositories.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Excepciones para USERS

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<String> handleUserNotExistsException(UserNotExistsException e) {
        // Manejar excepción de usuario no encontrado
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> handleUserExistsException(UserExistsException e) {
        // Manejar excepción de usuario existente
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario existente: " + e.getMessage());
    }

    // Excepciones para ACCOUNTS

    @ExceptionHandler(AccountNotExistException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotExistException e) {
        // Manejar excepción de cuenta no encontrada
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
    }

    @ExceptionHandler(AccountExistException.class)
    public ResponseEntity<String> handleAccountFoundException(AccountExistException e) {
        // Manejar excepción de cuenta existente
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // De EX-AccountUtils
    public static Account findAccountByIdOrThrow(AccountRepository accountRepository, Long accountId, String errorMessage) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(errorMessage + accountId));
    }

    // Excepciones para TRANSFERENCIAS

    @ExceptionHandler(TransferNotExistException.class)
    public ResponseEntity<String> handleTransferNotFoundException(TransferNotExistException e) {
        // Manejar excepción de transferencia no encontrado
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transferencia no encontrada");
    }

    @ExceptionHandler(InsufficientFoundsException.class)
    public ResponseEntity<String> handleInsufficientFoundsException(InsufficientFoundsException e) {
        // Fondos insuficientes
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Fondos insuficientes");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // Mensaje personalizado de error
        return "Hay un caracter inválido en la operacion. Verifica los datos ingresados.";
    }

}

