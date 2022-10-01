package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.CreditCard;
import com.alvarorivas.finalproject.repository.accounts.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditCardServiceImpl implements CreditCardService{

    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Optional<CreditCard> findById(Integer accountId) {
        return creditCardRepository.findById(accountId);
    }

    @Override
    public CreditCard createAccount(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public CreditCard updateAccount(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        creditCardRepository.deleteById(accountId);
    }
}
