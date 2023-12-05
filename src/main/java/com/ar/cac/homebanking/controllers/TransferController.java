package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.exceptions.InsufficientFoundsException;
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


    /* getTransferById ORIGINAL
    @GetMapping(value = "/{id}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id){
        TransferDTO transfer = service.getTransferById(id);
        return ResponseEntity.status(HttpStatus.OK).body(transfer);
    }
*/

    // GET TRANSFER BY ID con try-catch (Martin)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTransferById(@PathVariable Long id) {
        try {
            TransferDTO transferDTO = service.getTransferById(id);
            return ResponseEntity.status(HttpStatus.OK).body(service.getTransferById(id));
        } catch (TransferNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transferencia no encontrada: " + e.getMessage());
        }
    }

    /* PERFORM TRANSFER ORIGINAL
    @PostMapping
    public ResponseEntity<TransferDTO> performTransfer(@RequestBody TransferDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.performTransfer(dto));
    }
    */

    // PERFORM TRANSFER con try-catch (Martin)
    @PostMapping
    public ResponseEntity<?> performTransfer(@RequestBody TransferDTO dto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(service.performTransfer(dto));
        }catch (InsufficientFoundsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede hacer la transferencia: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en formato: " + e.getMessage());
        }
    }

    /* UPDATE TRANSFER ORIGINAL
    @PutMapping(value = "/{id}")
    public ResponseEntity<TransferDTO> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transfer){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateTransfer(id, transfer));
    }
*/

    // UPDATE TRANSFER con try-catch (Martin)
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transfer){
        try{
            service.updateTransfer(id, transfer);
            return ResponseEntity.status(HttpStatus.OK).body(service.updateTransfer(id, transfer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la operacion:" + e.getMessage());
        }

    }

    /* DELETE TRANSFER ORIGINAL

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteTransfer(id));
    }
    */

    //DELETE TRANSFER con try-catch (Martin)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable Long id){
        try{
            service.deleteTransfer(id);
            return ResponseEntity.status(HttpStatus.OK).body("Transferencia con id "+id+" eliminada correctamente");
        } catch (TransferNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transferencia Inexistente" + e.getMessage());
        }

    }

}
