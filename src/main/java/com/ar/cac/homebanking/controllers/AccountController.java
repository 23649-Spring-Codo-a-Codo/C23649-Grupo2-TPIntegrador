package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.dtos.TransactionDTO;
import com.ar.cac.homebanking.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAccount(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateAccount(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteAccount(id));
    }


    //express cashier
    @PostMapping("/withdraw/{id}")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable Long id, @RequestBody TransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body( service.withdraw(id, dto));

    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long id, @RequestBody TransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.deposit(id,dto));
    } //no logro
    @GetMapping("/balance/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(service.getBalance(id));
    }


}
