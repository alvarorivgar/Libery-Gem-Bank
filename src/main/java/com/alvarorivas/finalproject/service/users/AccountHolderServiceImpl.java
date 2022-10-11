package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.repository.users.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        if (accountHolderRepository.findById(accountHolder.getAccountHolderId()).isPresent()){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        return accountHolderRepository.save(accountHolder);
    }

    @Override
    public AccountHolder updateAccHolder(Integer id, AccountHolder accountHolder) {

        Optional<AccountHolder> storedAccHolder = accountHolderRepository.findById(id);

        if(storedAccHolder.isPresent()){

            storedAccHolder.get().setBirthDate(accountHolder.getBirthDate());
            storedAccHolder.get().setName(accountHolder.getName());
            storedAccHolder.get().setMailingAddress(accountHolder.getMailingAddress());
            storedAccHolder.get().setPrimaryAddress(accountHolder.getPrimaryAddress());

            return accountHolderRepository.save(storedAccHolder.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
    }

    @Override
    public void deleteAccHolder(Integer id) {

        Optional<AccountHolder> storedAccHolder = accountHolderRepository.findById(id);

        if(storedAccHolder.isPresent()){

            accountHolderRepository.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }

    }
}
