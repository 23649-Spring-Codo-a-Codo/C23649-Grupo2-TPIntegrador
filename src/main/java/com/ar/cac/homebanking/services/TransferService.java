package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.InsufficientFoundsException;
import com.ar.cac.homebanking.exceptions.TransferNotFoundException;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.mappers.TransferMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.Transfer;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.dtos.TransactionDTO;
import com.ar.cac.homebanking.models.dtos.TransferDTO;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.TransferRepository;
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

    public TransferDTO getTransferById(Long id){
        Transfer transfer = repository.findById(id).orElseThrow(() ->
                new TransferNotFoundException("Transfer not found with id: " + id));
        return TransferMapper.transferToDto(transfer);
    }

    public TransferDTO updateTransfer(Long id, TransferDTO transferDto){
        Transfer transfer = repository.findById(id).orElseThrow(() -> new TransferNotFoundException("Transfer not found with id: " + id));
        Transfer updatedTransfer = TransferMapper.dtoToTransfer(transferDto);
        updatedTransfer.setId(transfer.getId());
        return TransferMapper.transferToDto(repository.save(updatedTransfer));
    }

    public String deleteTransfer(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "Se ha eliminado la transferencia";
        } else {
            return "No se ha eliminado la transferencia";
        }
    }

    @Transactional
    public TransferDTO performTransfer(TransferDTO dto) {
        // Comprobar si las cuentas de origen y destino existen
        Account originAccount = accountRepository.findById(dto.getOrigin())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getOrigin()));
        Account destinationAccount = accountRepository.findById(dto.getTarget())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getTarget()));

        // Comprobar si la cuenta de origen tiene fondos suficientes
        if (originAccount.getAmount().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientFoundsException("Insufficient funds in the account with id: " + dto.getOrigin());
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


    //metodos para retiro y deposito que se guardará como una transferencia mas
    public TransferDTO withdraw(TransferDTO dto) {

        Transfer transfer = new Transfer();
        if (validateAccount(dto.getOrigin()) && checkFounds(dto.getOrigin(), dto.getAmount())){
            Account accountToModify = accountRepository.findById(dto.getOrigin()).get();
            accountToModify.setAmount(accountToModify.getAmount().subtract(dto.getAmount()));
            accountRepository.save(accountToModify);
            // Seteamos el objeto fecha actual en el transferDto
            transfer.setDate(new Date());
            transfer.setOrigin(dto.getOrigin());
            transfer.setTarget(dto.getTarget());
            transfer.setAmount(dto.getAmount());
            transfer = repository.save(transfer);

        }
        return TransferMapper.transferToDto(transfer);
    }

    public TransferDTO deposit(TransferDTO dto) {

        Transfer transfer = new Transfer();
        if (validateAccount(dto.getOrigin())){
            Account accountToModify = accountRepository.findById(dto.getOrigin()).get();
            accountToModify.setAmount(accountToModify.getAmount().add(dto.getAmount()));
            accountRepository.save(accountToModify);
            // Seteamos el objeto fecha actual en el transferDto
            transfer.setDate(new Date());
            transfer.setOrigin(dto.getOrigin());
            transfer.setTarget(dto.getTarget());
            transfer.setAmount(dto.getAmount());
            transfer = repository.save(transfer);

        }
        return TransferMapper.transferToDto(transfer);
    }
    public Boolean validateAccount(Long id) {
        accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("La cuenta con id: " +id+" no existe"));
        return true;
    }

    public Boolean checkFounds (Long id, BigDecimal amount) {
        Account accountToCheck = accountRepository.findById(id).get();

        if (accountToCheck.getAmount().compareTo(amount) >= 0) {
            return true; //fondos suficientes
        } else {
            throw new InsufficientFoundsException("Fondos Insuficientes en la cuenta con id: " + id);
        }

    }

}
