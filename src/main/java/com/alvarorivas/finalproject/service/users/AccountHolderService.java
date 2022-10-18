package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import java.util.Optional;

public interface AccountHolderService {

    Optional<AccountHolder> findById(Integer id);

    AccountHolder createAccHolder(AccountHolder accountHolder);

    AccountHolder updateAccHolder(Integer id, AccountHolder accountHolder);

    void deleteAccHolder(Integer id);
}
