package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.CreditCard;
import com.alvarorivas.finalproject.model.util.Money;

import java.util.Optional;

public interface CreditCardService {

    Optional<CreditCard> findById(Integer accountId);

    CreditCard createAccount(CreditCard creditCard);

    CreditCard updateBalance(Integer id, Money balance); //Admins should be able to access the balance for any account and to modify it.

    CreditCard updateAccount(Integer id, CreditCard creditCard);

    void deleteAccount(Integer id);

    void applyInterest(Integer id);

    Money checkBalance(Integer id);

    public Integer accountTypeChecker(Integer id);

    void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount);

}
