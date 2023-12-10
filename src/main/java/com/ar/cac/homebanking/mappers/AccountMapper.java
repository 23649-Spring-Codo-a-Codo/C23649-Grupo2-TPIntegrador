package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.repositories.UserRepository;
import lombok.experimental.UtilityClass;
import com.ar.cac.homebanking.repositories.UserRepository;

@UtilityClass
public class AccountMapper {
    private UserRepository userRepository;
    // TODO: REFACTOR BUILDER
    public AccountDTO accountToDto(Account account){
        AccountDTO dto = new AccountDTO();
        dto.setAlias(account.getAlias());
        dto.setCbu(account.getCbu());
        dto.setType(account.getType());
        dto.setAmount(account.getAmount());
        dto.setId(account.getId());
        dto.setOwnerId(account.getOwner().getId());
        return dto;
    }

    public Account dtoToAccount(AccountDTO dto, UserRepository userRepository){
        Account account = new Account();
        account.setAlias(dto.getAlias());
        account.setType(dto.getType());
        account.setCbu(dto.getCbu());
        account.setAmount(dto.getAmount());
        account.setOwner(userRepository.getById(dto.getOwnerId()));


        return account;
    }
}
