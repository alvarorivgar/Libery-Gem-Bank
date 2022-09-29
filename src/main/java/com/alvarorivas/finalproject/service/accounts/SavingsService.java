package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Savings;

import java.util.Optional;

public interface SavingsService {

    Optional<Savings> findById(Integer accountId);

    Savings createAccount(Savings savings);

    Savings updateAccount(Savings savings); //Admins should be able to access the balance for any account and to modify it.

    void deleteAccount(Integer accountId);

}
