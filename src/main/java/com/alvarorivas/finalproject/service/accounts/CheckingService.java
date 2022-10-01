package com.alvarorivas.finalproject.service.accounts;


import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;

import java.util.Optional;

public interface CheckingService {

    Optional<Checking> findById(Integer accountId);

    Checking createAccount(Checking checking);

    Checking updateAccount(Checking checking); //Admins should be able to access the balance for any account and to modify it.

    void deleteAccount(Integer accountId);

   // Money checkBalance(Checking checking);

   // Checking transferMoney(Checking origin, AccountHolder receiverName, Integer receiverId);


}
