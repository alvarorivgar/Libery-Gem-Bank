package com.alvarorivas.finalproject.service.accounts;


import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.util.Money;

import java.util.Optional;

public interface CheckingService {

    Optional<Checking> findById(Integer id);

    Account createAccount(Checking checking);

    Checking updateBalance(Integer id, Money balance); //Admins should be able to access the balance for any account and to modify it.
    Checking updateAccount(Integer id, Checking checking);

    void deleteAccount(Integer id);

    void applyPenaltyFee(Integer id);

    void applyMonthlyMaintenanceFee(Integer id);

    Money checkBalance(Integer id);

    void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount);


}
