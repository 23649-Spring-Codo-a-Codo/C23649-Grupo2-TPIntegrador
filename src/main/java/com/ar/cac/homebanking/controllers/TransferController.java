package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id){
        TransferDTO transfer = service.getTransferById(id);
        return ResponseEntity.status(HttpStatus.OK).body(transfer);
    }


    @PostMapping
    public ResponseEntity<TransferDTO> performTransfer(@RequestBody TransferDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED).body(service.performTransfer(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TransferDTO> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transfer){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateTransfer(id, transfer));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteTransfer(id));
    }

    //express cashier
    @PostMapping("/withdraw/{id}")
    public ResponseEntity<TransferDTO> withdraw(@PathVariable Long id, @RequestBody TransferDTO dto)
    {
        dto.setOrigin(id);
        dto.setTarget(id);
        return ResponseEntity.status(HttpStatus.OK).body( service.withdraw(dto));

    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<TransferDTO> deposit(@PathVariable Long id, @RequestBody TransferDTO dto) {
        dto.setOrigin(id);
        dto.setTarget(id); //no se que tan bueno respecto a seguridad sea este seteo!!
        return ResponseEntity.status(HttpStatus.OK).body(service.deposit(dto));
    }
}
