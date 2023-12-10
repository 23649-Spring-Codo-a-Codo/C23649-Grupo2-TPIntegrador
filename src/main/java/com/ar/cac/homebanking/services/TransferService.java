package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.InsufficientFoundsException;
import com.ar.cac.homebanking.exceptions.TransferNotFoundException;
import com.ar.cac.homebanking.exceptions.TypeDataErrorException;
import com.ar.cac.homebanking.mappers.TransferMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.Transfer;
import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.TransferRepository;
import com.ar.cac.homebanking.tools.AccountUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    private final TransferRepository repository;

    private final AccountRepository accountRepository;

    public TransferService(TransferRepository repository, AccountRepository accountRepository){
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public List<TransferDTO> getTransfers(){
        List<Transfer> transfers = repository.findAll();
        return transfers.stream()
                .map(TransferMapper::transferToDto)
                .collect(Collectors.toList());
    }

    /*public TransferDTO getTransferById(Long id){
        Transfer transfer = repository.findById(id).orElseThrow(() ->
                new TransferNotFoundException("Transfer not found with id: " + id));
        return TransferMapper.transferToDto(transfer);
    }*/
    public TransferDTO getTransferById(Long id) {
        if (repository.existsById(id)) {
            Transfer transfer = repository.findById(id).get();
            return TransferMapper.transferToDto(transfer);
        } else {
            throw new TransferNotFoundException("La Transferencia con el id " + id + " no existe");
        }
    }


    public TransferDTO updateTransfer(Long id, TransferDTO transferDto){

        Account originAccount = AccountUtils.findAccountByIdOrThrow(accountRepository, transferDto.getOrigin(), "La cuenta origen no existe id: ");
        Account destinationAccount = AccountUtils.findAccountByIdOrThrow(accountRepository, transferDto.getTarget(), "La cuenta destino no existe id: ");

        Transfer transfer = repository.findById(id).orElseThrow(() -> new TransferNotFoundException("La transferencia con el id " + id + " no existe"));
        Transfer updatedTransfer = TransferMapper.dtoToTransfer(transferDto);
        updatedTransfer.setId(transfer.getId());
        return TransferMapper.transferToDto(repository.save(updatedTransfer));
    }

    public void deleteTransfer(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
           } else {
            throw new TransferNotFoundException ("No se puede eliminar la transferencia con el id "+id+" porque no existe");
        }
    }

    @Transactional
    public TransferDTO performTransfer(TransferDTO dto) {

        Account originAccount = AccountUtils.findAccountByIdOrThrow(accountRepository, dto.getOrigin(), "La cuenta origen no existe id: ");
        Account destinationAccount = AccountUtils.findAccountByIdOrThrow(accountRepository, dto.getTarget(), "La cuenta destino no existe id: ");

/*        Account originAccount = accountRepository.findById(dto.getOrigin())
          .orElseThrow(() -> new AccountNotFoundException("La cuenta origen no existe id: " + dto.getOrigin()));
        Account destinationAccount = accountRepository.findById(dto.getTarget())
          .orElseThrow(() -> new AccountNotFoundException("La cuenta destino no existe id: " + dto.getTarget()));*/

        // Comprobar si la cuenta de origen tiene fondos suficientes
        if (originAccount.getAmount().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientFoundsException("Fondos Insuficientes en la cuenta con id: " + dto.getOrigin());
        }
        //Comprobar formato correcto
        if (!isBigDecimalType(dto.getAmount())) {
            throw new IllegalArgumentException("La Transferencia no puede ser nula ni menor a 1");
        }


        // Realizar la transferencia
        originAccount.setAmount(originAccount.getAmount().subtract(dto.getAmount()));
        destinationAccount.setAmount(destinationAccount.getAmount().add(dto.getAmount()));

        // Guardar las cuentas actualizadas
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        // Crear la transferencia y guardarla en la base de datos
        Transfer transfer = new Transfer();
        // Creamos un objeto del tipo Date para obtener la fecha actual
        Date date = new Date();
        // Seteamos el objeto fecha actual en el transferDto
        transfer.setDate(date);
        transfer.setOrigin(originAccount.getId());
        transfer.setTarget(destinationAccount.getId());
        transfer.setAmount(dto.getAmount());
        transfer = repository.save(transfer);

        // Devolver el DTO de la transferencia realizada
        return TransferMapper.transferToDto(transfer);

    }

    /*private boolean isBigDecimalType(Object obj) {
        if ( obj instanceof BigDecimal){
            BigDecimal amount = (BigDecimal) obj;
            return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
        }
        return false;
    }*/
    private boolean isBigDecimalType(Object obj) {

            // Verificar si obj(amount) es un BigDecimal y si es mayor que cero
            if (obj instanceof BigDecimal) {
                BigDecimal amount = (BigDecimal) obj;
                return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
            }

        return false;
    }

    /*private void validateAccountsExist(Long originAccountId, Long destinationAccountId) {
        if (!accountRepository.existsById(originAccountId)) {
            throw new AccountNotFoundException("Cuenta de origen no encontrada con id: " + originAccountId);
        }

        if (!accountRepository.existsById(destinationAccountId)) {
            throw new AccountNotFoundException("Cuenta de destino no encontrada con id: " + destinationAccountId);
        }
    }*/




}
