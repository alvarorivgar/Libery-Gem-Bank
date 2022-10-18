package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.repository.users.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements AccountHolderService{

    @Autowired
    AccountHolderRepository accountHolderRepository;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<AccountHolder> findById(Integer id) {
        return accountHolderRepository.findById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AccountHolder createAccHolder(AccountHolder accountHolder) {

        return accountHolderRepository.save(accountHolder);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccHolder(Integer id) {

        Optional<AccountHolder> storedAccHolder = accountHolderRepository.findById(id);

        if(storedAccHolder.isPresent()){

            accountHolderRepository.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }

    }
}
