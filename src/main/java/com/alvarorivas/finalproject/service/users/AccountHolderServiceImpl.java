package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.repository.users.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements AccountHolderService{

    @Autowired
    AccountHolderRepository accountHolderRepository;


    @Override
    public Optional<AccountHolder> findById(Integer id) {
        return accountHolderRepository.findById(id);
    }

    @Override
    public AccountHolder createAccHolder(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    @Override
    public AccountHolder updateAccHolder(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    @Override
    public void deleteAccHolder(Integer id) {
        accountHolderRepository.deleteById(id);
    }
}
