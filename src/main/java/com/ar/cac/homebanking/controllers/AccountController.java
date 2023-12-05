package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.exceptions.AccountExistException;
import com.ar.cac.homebanking.exceptions.AccountNotExistException;
import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts(){
        List<AccountDTO> lista = service.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }
/*
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAccountById(id));
    }
*/
    // GET Account BY ID con try-catch (Alejandra)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        try {
            service.getAccountById(id);
            return ResponseEntity.status(HttpStatus.OK).body(service.getAccountById(id));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada: " + e.getMessage());
        }

    }

// FALTAN los try-catch de los siguientes metodos (Ale)

/*
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAccount(dto));
    }
*/

    //CREATE Account BY ID con try-catch (Claudia)

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO account) {
        try {
            AccountDTO accountCreated = service.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountCreated);
        } catch (AccountExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

/*

    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateAccount(id, dto));
    }
*/

    //UPDATE Account BY ID con try-catch (Claudia)

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
        try {
            service.updateAccount(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Cuenta modificada exitosamente");
        } catch (AccountNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta a modificar no encontrada: " + e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: no se pueden enviar datos nulos: " + e.getMessage());
        }
    }

    /*
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteAccount(id));
    }
    */


    //DELETE Account con try-catch (Alejandra)
    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        try {
            service.deleteAccount(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cuenta borrada correctamente");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta a borrar no encontrada: " + e.getMessage());
        }

    }


}
