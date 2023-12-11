package com.ar.cac.homebanking.tools;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.repositories.AccountRepository;

public class AccountUtils {

    public static Account findAccountByIdOrThrow(AccountRepository accountRepository, Long accountId, String errorMessage) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(errorMessage + accountId));
    }
}