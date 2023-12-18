package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.exceptions.*;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.dtos.UserDTO;
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

    // GET ALL ACCOUNTS
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts(){
        List<AccountDTO> lista = service.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    // GET ACCOUNT BY ID con GlobalExceptionHandler (Alejandra)
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAccountById(id));
    }

    // CREATE ACCOUNT con GlobalExceptionHandler (Alejandra)
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO account) {
        AccountDTO accountCreated = service.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreated);
    }

    // UPDATE ACCOUNT con GlobalExcepcionHandler (Alejandra)
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
        service.updateAccount(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Cuenta modificada exitosamente");
    }

    // DELETE ACCOUNT con GlobalExcepcionHandler (Alejandra)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body("Cuenta eliminada correctamente");
    }
}
