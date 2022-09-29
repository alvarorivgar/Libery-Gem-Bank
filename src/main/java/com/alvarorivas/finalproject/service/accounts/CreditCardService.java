package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.CreditCard;

import java.util.Optional;

public interface CreditCardService {

    Optional<CreditCard> findById(Integer accountId);

    CreditCard createAccount(CreditCard creditCard);

    CreditCard updateAccount(CreditCard creditCard); //Admins should be able to access the balance for any account and to modify it.

    void deleteAccount(Integer accountId);

}
