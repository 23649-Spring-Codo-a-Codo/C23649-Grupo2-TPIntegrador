package com.ar.cac.homebanking.controllers;


import com.ar.cac.homebanking.tools.ExceptionHandler;
import com.ar.cac.homebanking.exceptions.TransferNotFoundException;
import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransferDTO>> getTransfers(){
        List<TransferDTO> transfers = service.getTransfers();
        return ResponseEntity.status(HttpStatus.OK).body(transfers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTransferById(@PathVariable Long id) {
        try {
            TransferDTO transferDTO = service.getTransferById(id);
            return ResponseEntity.status(HttpStatus.OK).body(service.getTransferById(id));
        } catch (TransferNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transferencia no encontrada: " + e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<?> performTransfer(@RequestBody TransferDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.performTransfer(dto));
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> performDeposit(@RequestBody TransferDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.performDeposit(dto));
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> performWithdraw(@RequestBody TransferDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.performWithDraw(dto));
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, e.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transfer){
        try{
            service.updateTransfer(id, transfer);
            return ResponseEntity.status(HttpStatus.OK).body(service.updateTransfer(id, transfer));
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, e.getMessage());
        }/*catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la operacion:" + e.getMessage());
        }*/

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTransfer(@PathVariable Long id){
        try{
            service.deleteTransfer(id);
            return ResponseEntity.status(HttpStatus.OK).body("Transferencia con id "+id+" eliminada correctamente");
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, e.getMessage());
        }

    }
}
