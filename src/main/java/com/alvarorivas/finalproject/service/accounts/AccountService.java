package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> findById(Integer id);
}
