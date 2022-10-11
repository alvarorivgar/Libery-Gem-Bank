package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Savings;
import com.alvarorivas.finalproject.model.util.Money;

import java.util.Optional;

public interface SavingsService {

    Optional<Savings> findById(Integer id);

    Savings createAccount(Savings savings);

    Savings updateBalance(Integer id, Money balance); //Admins should be able to access the balance for any account and to modify it.

    Savings updateAccount(Integer id, Savings savings);

    void deleteAccount(Integer id);

    void applyPenaltyFee(Integer id);

    void applyInterest(Integer id);

    Money checkBalance(Integer id);

    public Integer accountTypeChecker(Integer id);

    void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount);

}
