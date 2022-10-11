package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.util.Money;

import java.util.Optional;

public interface StudentCheckingService {

    Optional<StudentChecking> findById(Integer id);

    StudentChecking updateBalance(Integer id, Money balance); //Admins should be able to access the balance for any account and to modify it.

    StudentChecking updateAccount(Integer id, StudentChecking studentChecking);

    void deleteAccount(Integer id);

    Money checkBalance(Integer id);

    public Integer accountTypeChecker(Integer id);

    void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount);

}