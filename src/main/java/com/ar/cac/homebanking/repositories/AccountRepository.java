package com.ar.cac.homebanking.repositories;

import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByCbu(String cbu);
}
