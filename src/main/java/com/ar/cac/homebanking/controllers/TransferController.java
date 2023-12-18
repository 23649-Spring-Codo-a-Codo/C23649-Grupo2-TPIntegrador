package com.ar.cac.homebanking.controllers;

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

    // GET ALL TRANSFERS
    @GetMapping
    public ResponseEntity<List<TransferDTO>> getTransfers(){
        List<TransferDTO> transfers = service.getTransfers();
        return ResponseEntity.status(HttpStatus.OK).body(transfers);
    }

    // GET TRANSFER BY ID con GlobalExceptionHandler (Alejandra)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTransferById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getTransferById(id));
    }

    // REALIZAR TRANSFERENCIA con GlobalExceptionHandler (Alejandra)
    @PostMapping
    public ResponseEntity<TransferDTO> performTransfer(@RequestBody TransferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.performTransfer(dto));
    }

    // REALIZAR DEPOSITO con GlobalExceptionHandler (Alejandra)
    @PostMapping("/deposit")
    public ResponseEntity<TransferDTO> performDeposit(@RequestBody TransferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.performDeposit(dto));
    }

    // REALIZAR EXTRACCION con GlobalExceptionHandler (Alejandra)
    @PostMapping("/withdraw")
    public ResponseEntity<TransferDTO> performWithdraw(@RequestBody TransferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.performWithDraw(dto));
    }

    // ACTUALIZAR TRANSFERENCIA con GlobalExceptionHandler (Alejandra)
    @PutMapping(value = "/{id}")
    public ResponseEntity<TransferDTO> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transfer){
        service.updateTransfer(id, transfer);
        return ResponseEntity.status(HttpStatus.OK).body(service.updateTransfer(id, transfer));
    }

    // DELETE TRANSFER con con GlobalExceptionHandler (Alejandra)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTransfer(@PathVariable Long id){
        service.deleteTransfer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Transferencia con id "+id+" eliminada correctamente");
    }
}
