package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.*;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.*;
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
    SavingsRepository savingsRepository;

   @Autowired
    CreditCardRepository creditCardRepository;

   @Autowired
   StudentCheckingService studentCheckingService;


   @Override
   public Optional<Checking> findById(Integer id) {

       return checkingRepository.findById(id);
   }

   @Override
   public Account createAccount(Checking checking) {

       //If account owner is less than 24 years old, create a student checking account
       if(checking.getPrimaryOwner().getBirthDate().isAfter(LocalDate.now().minusYears(24))){

           return studentCheckingService.fromChecking(checking);

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
                storedChecking.get().setLastMaintenanceFeeApplication(checking.getLastMaintenanceFeeApplication());
                storedChecking.get().setLastPenaltyFeeCheck(checking.getLastPenaltyFeeCheck());

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

            while(LocalDate.now().isAfter(storedChecking.get().getLastPenaltyFeeCheck().plusMonths(1))){

                //Adds 1 month to last penalty fee check
                storedChecking.get().setLastPenaltyFeeCheck(storedChecking.get().getLastPenaltyFeeCheck().plusMonths(1));

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
            while(LocalDate.now().isAfter(storedChecking.get().getLastPenaltyFeeCheck().plusMonths(1))){
                //Adds 1 month to last maintenance fee application
                storedChecking.get().setLastMaintenanceFeeApplication(storedChecking.get().getLastMaintenanceFeeApplication().plusMonths(1));
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

    public Integer accountTypeChecker(Integer id) {

        Optional<Checking> checking = checkingRepository.findById(id);
        Optional<CreditCard> creditCard = creditCardRepository.findById(id);
        Optional<Savings> savings = savingsRepository.findById(id);
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(id);

        if (checking.isPresent()) {
            return 1;
        } else if (creditCard.isPresent()) {
            return 2;
        } else if (savings.isPresent()) {
            return 3;
        } else if (studentChecking.isPresent()) {
            return 4;
        }else {
            return null;
        }
    }

    @Override
    public void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount) {

       Optional<Checking> originAccount = checkingRepository.findById(originId);

       Integer receiverAccount = accountTypeChecker(receiverId);


       if(!originAccount.isPresent()){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");}

       if(receiverAccount == null){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");
       }

       //Check if balance is updated
       applyPenaltyFee(originId);
       applyMonthlyMaintenanceFee(originId);

       //Check if amount to transfer is lower than origin account's current balance
        if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }


        originAccount.get().getBalance().decreaseAmount(amount);
        checkingRepository.save(originAccount.get());

        switch (receiverAccount) {
            case 1 -> {
                           Checking receiverChecking = checkingRepository.findById(receiverId).get();
                           receiverChecking.getBalance().increaseAmount(amount);
                           checkingRepository.save(receiverChecking);
            }
            case 2 -> {
                           CreditCard receiverCard = creditCardRepository.findById(receiverId).get();
                           receiverCard.getBalance().increaseAmount(amount);
                           creditCardRepository.save(receiverCard);
            }
            case 3 -> {
                           Savings receiverSavings = savingsRepository.findById(receiverId).get();
                           receiverSavings.getBalance().increaseAmount(amount);
                           savingsRepository.save(receiverSavings);
            }
            case 4 -> {
                           StudentChecking receiverStudent = studentCheckingRepository.findById(receiverId).get();
                           receiverStudent.getBalance().increaseAmount(amount);
                           studentCheckingRepository.save(receiverStudent);
            }
        }
    }
}
