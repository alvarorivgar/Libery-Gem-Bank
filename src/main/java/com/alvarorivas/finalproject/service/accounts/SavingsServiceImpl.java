package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Savings;
import com.alvarorivas.finalproject.repository.accounts.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SavingsServiceImpl implements SavingsService{

    @Autowired
    SavingsRepository savingsRepository;


    @Override
    public Optional<Savings> findById(Integer accountId) {
        return savingsRepository.findById(accountId);
    }

    @Override
    public Savings createAccount(Savings savings) {
        return savingsRepository.save(savings);
    }

    @Override
    public Savings updateAccount(Savings savings) {
        return savingsRepository.save(savings);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        savingsRepository.deleteById(accountId);
    }
}
