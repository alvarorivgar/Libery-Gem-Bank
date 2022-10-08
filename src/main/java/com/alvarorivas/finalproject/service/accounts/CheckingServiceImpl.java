package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.AccountRepository;
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

   @Autowired
    AccountRepository accountRepository;

   @Override
   public Optional<Checking> findById(Integer id) {

       return checkingRepository.findById(id);
   }

   @Override
   public Account createAccount(Checking checking) {

       if(checkingRepository.findById(checking.getAccountId()).isPresent()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exists");
       }

       //If account owner is less than 24 years old, create a student checking account
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
   }

   @Override
   public void deleteAccount(Integer id) {

       Optional<Checking> storedChecking = checkingRepository.findById(id);

       if (storedChecking.isPresent()) {
           checkingRepository.delete(storedChecking.get());
       } else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
       }
   }

    @Override
    public void applyPenaltyFee(Integer id) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {

            while(LocalDate.now().isAfter(storedChecking.get().getLastFeeApplication().plusMonths(1))){

                //Check if current balance is lower than minimum balance
                if (storedChecking.get().getBalance().getAmount().compareTo(storedChecking.get().getMinimumBalance().getAmount()) == -1) {
                    //Subtract penalty fee to balance
                    storedChecking.get().getBalance().decreaseAmount(storedChecking.get().getPenaltyFee());

                    checkingRepository.save(storedChecking.get());
                }
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void applyMonthlyMaintenanceFee(Integer id) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {
            while(LocalDate.now().isAfter(storedChecking.get().getLastFeeApplication().plusMonths(1))){
                //Adds 1 month to last maintenance fee application
                storedChecking.get().setLastFeeApplication(storedChecking.get().getLastFeeApplication().plusMonths(1));
                //Subtracts maintenance fee to balance
                storedChecking.get().setBalance(new Money(storedChecking.get().getBalance().decreaseAmount(storedChecking.get().getMonthlyMaintenanceFee().getAmount())));

                checkingRepository.save(storedChecking.get());
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public Money checkBalance(Integer id) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);

        if (storedChecking.isPresent()) {

            //Applies monthly maintenance fee and penalty fee if applicable to update balance
            applyPenaltyFee(id);
            applyMonthlyMaintenanceFee(id);

            checkingRepository.save(storedChecking.get());

            return storedChecking.get().getBalance();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount) {

       Optional<Checking> originAccount = checkingRepository.findById(originId);
       Optional<Account> receiverAccount = accountRepository.findById(receiverId);

       if(originAccount.isPresent()){

           if(receiverAccount.isPresent()){

               //Check if balance is updated
               applyPenaltyFee(originId);
               applyMonthlyMaintenanceFee(originId);


                //Check if amount to transfer is lower than origin account's current balance
               if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == -1){


                   originAccount.get().getBalance().decreaseAmount(amount);
                   receiverAccount.get().getBalance().increaseAmount(amount);
                   checkingRepository.save(originAccount.get());
                   accountRepository.save(receiverAccount.get());


               }else {
                   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
                }
           }else {
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");
             }
       }else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");
        }
    }
}
