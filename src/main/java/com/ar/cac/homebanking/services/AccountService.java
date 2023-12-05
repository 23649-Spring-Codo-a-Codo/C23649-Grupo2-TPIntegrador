package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.AccountExistException;
import com.ar.cac.homebanking.exceptions.AccountNotExistException;
import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.models.enums.AccountType;
import com.ar.cac.homebanking.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository){
        this.repository = repository;
    }
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = repository.findAll();
        return accounts.stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList());
    }



    public AccountDTO createAccount(AccountDTO accountDto) throws AccountExistException {
        Account accountExists = accountExistByCbu(accountDto);
        // TODO: REFACTOR

        // Dentro del if moví el setType para que la cuenta creada sea del tipo SAVINGS_BANK (Alejandra)

        if (accountExists == null){
            accountDto.setType(AccountType.SAVINGS_BANK);
            accountDto.setAmount(BigDecimal.ZERO);
            Account newAccount = repository.save(AccountMapper.dtoToAccount(accountDto));
            return AccountMapper.accountToDto(newAccount);
        } else {
            throw new AccountExistException("La cuenta ya existe: " + accountDto.getCbu());
        }
    }

   /* public AccountDTO getAccountById(Long id) {
        Account entity = repository.findById(id).get();
        return AccountMapper.accountToDto(entity);
    }*/

    public AccountDTO getAccountById(Long id) {
        if (repository.existsById(id)) {
            Account entity = repository.findById(id).get();
            return AccountMapper.accountToDto(entity);
        } else {
            throw new AccountNotFoundException("Cuenta inexistente");
        }

    }

    public String deleteAccount(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "La cuenta con id: " + id + " ha sido eliminada";
        } else {
            // TODO: REFACTOR create new exception
            throw new AccountNotFoundException("La cuenta a eliminar no existe");
        }

    }

    // UPDATE Account con throw (Claudia)
    public AccountDTO updateAccount(Long id, AccountDTO dto) {
        if (repository.existsById(id)) {
            Account accountToModify = repository.findById(id).get();
            // Validar qué datos no vienen en null para setearlos al objeto ya creado
            if (dto.getAlias() != null || dto.getType() != null || dto.getCbu() != null || dto.getAmount() != null) {
                // Logica del Patch
                if (dto.getAlias() != null) {
                    accountToModify.setAlias(dto.getAlias());
                }

                if (dto.getType() != null) {
                    accountToModify.setType(dto.getType());
                }

                if (dto.getCbu() != null) {
                    accountToModify.setCbu(dto.getCbu());
                }

                if (dto.getAmount() != null) {
                    accountToModify.setAmount(dto.getAmount());
                }

                Account accountModified = repository.save(accountToModify);

                return AccountMapper.accountToDto(accountModified);
            }else {
                throw new IllegalArgumentException("Al menos un campo obligatorio debe estar presente para la actualización.");
            }
        }else{
            throw new AccountNotExistException("La cuenta no existe");
        }
    }

    public Account accountExistByCbu(AccountDTO dto){
        return repository.findByCbu(dto.getCbu());
    }
}
