package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
import com.alvarorivas.finalproject.repository.accounts.StudentCheckingRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CheckingServiceImpl implements CheckingService{

   @Autowired
    CheckingRepository checkingRepository;

   @Autowired
    StudentCheckingRepository studentCheckingRepository;

   @Override
   public Optional<Checking> findById(Integer accountId) {

       return checkingRepository.findById(accountId);
   }

   @Override
   public Account createAccount(Checking checking) {

       if(checking.getPrimaryOwner().getBirthDate().isAfter(LocalDate.now().minusYears(24))){

           Gson gson = new Gson();
           String jsonString = gson.toJson(checking);

           StudentChecking studentChecking = gson.fromJson(jsonString, StudentChecking.class);

           return studentCheckingRepository.save(studentChecking);
       }else {

           return checkingRepository.save(checking);
       }
   }

    @Override
    public Checking updateBalance(Integer id, Money balance) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {

                storedChecking.get().setBalance(balance);
                return checkingRepository.save(storedChecking.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
    }

    @Override
   public Checking updateAccount(Integer id, Checking checking) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {

                storedChecking.get().setBalance(checking.getBalance());
                storedChecking.get().setMinimumBalance(checking.getMinimumBalance());
                storedChecking.get().setCreationDate(checking.getCreationDate());
                storedChecking.get().setPenaltyFee(checking.getPenaltyFee());
                storedChecking.get().setStatus(checking.getStatus());
                storedChecking.get().setPrimaryOwner(checking.getPrimaryOwner());
                storedChecking.get().setSecondaryOwner(checking.getSecondaryOwner());
                storedChecking.get().setMonthlyMaintenanceFee(checking.getMonthlyMaintenanceFee());
                storedChecking.get().setSecretKey(checking.getSecretKey());

                return checkingRepository.save(storedChecking.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
   }

   @Override
   public void deleteAccount(Integer id) {

       Optional<Checking> storedChecking = checkingRepository.findById(id);

       if (storedChecking.isPresent()) {
           checkingRepository.delete(storedChecking.get());
       } else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
       }
   }

    @Override
    public void checkPenaltyFee(Integer id) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {
            //Check if current balance is lower than minimum balance
            if (storedChecking.get().getBalance().getAmount().compareTo(storedChecking.get().getMinimumBalance().getAmount()) == -1) {
                //Subtract penalty fee to balance
                storedChecking.get().getBalance().decreaseAmount(storedChecking.get().getPenaltyFee().getAmount());
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
    }

    @Override
    public void applyMonthlyMaintenanceFee(Integer id) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {
            while(LocalDate.now().isAfter(storedChecking.get().getLastMaintenanceFeeApplication().plusMonths(1))){
                //Adds 1 month to last maintenance fee application
                storedChecking.get().setLastMaintenanceFeeApplication(storedChecking.get().getLastMaintenanceFeeApplication().plusMonths(1));
                //Subtracts maintenance fee to balance
                storedChecking.get().setBalance(new Money(storedChecking.get().getBalance().decreaseAmount(storedChecking.get().getMonthlyMaintenanceFee().getAmount())));
                //Check if balance drops below minimum and apply fee if applicable
                checkPenaltyFee(id);
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
    }

    @Override
    public Money checkBalance(Integer id) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {

            //Applies monthly maintenance fee and penalty fee if applicable to update balance
            applyMonthlyMaintenanceFee(id);

            return storedChecking.get().getBalance();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
    }



}
