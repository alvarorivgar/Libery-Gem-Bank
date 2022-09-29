package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckingServiceImpl implements CheckingService{

   @Autowired
    CheckingRepository checkingRepository;

   @Override
   public Optional<Checking> findById(Integer accountId) {
       return checkingRepository.findById(accountId);
   }

   @Override
   public Checking createAccount(Checking checking) {
       return checkingRepository.save(checking);
   }

   @Override
   public Checking updateAccount(Checking checking) {
       return checkingRepository.save(checking);
   }

   @Override
   public void deleteAccount(Integer accountId) {
        checkingRepository.deleteById(accountId);
   }
}
