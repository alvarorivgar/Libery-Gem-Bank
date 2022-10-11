package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.*;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class SavingsServiceImpl implements SavingsService{

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Optional<Savings> findById(Integer id) {
        return savingsRepository.findById(id);
    }

    @Override
    public Savings createAccount(Savings savings) {

        if(savingsRepository.findById(savings.getAccountId()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exists");
        }

        return savingsRepository.save(savings);
    }

    @Override
    public Savings updateBalance(Integer id, Money balance) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);

        if (storedSavings.isPresent()) {

            storedSavings.get().setBalance(balance);
            return savingsRepository.save(storedSavings.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public Savings updateAccount(Integer id, Savings savings) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);

        if (storedSavings.isPresent()) {

            storedSavings.get().setLastInterestApplication(savings.getLastInterestApplication());
            storedSavings.get().setMinimumBalance(savings.getMinimumBalance());
            storedSavings.get().setInterestRate(savings.getInterestRate());
            storedSavings.get().setBalance(savings.getBalance());
            storedSavings.get().setSecretKey(savings.getSecretKey());
            storedSavings.get().setCreationDate(savings.getCreationDate());
            storedSavings.get().setPenaltyFee(savings.getPenaltyFee());
            storedSavings.get().setMinimumBalance(savings.getMinimumBalance());
            storedSavings.get().setPrimaryOwner(savings.getPrimaryOwner());
            storedSavings.get().setSecondaryOwner(savings.getSecondaryOwner());
            storedSavings.get().setStatus(savings.getStatus());

            return savingsRepository.save(storedSavings.get());

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void deleteAccount(Integer id) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);

        if (storedSavings.isPresent()) {
            savingsRepository.delete(storedSavings.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void applyPenaltyFee(Integer id) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);

        if (storedSavings.isPresent()) {

            while(LocalDate.now().isAfter(storedSavings.get().getLastFeeApplication().plusMonths(1))){

                //Check if current balance is lower than minimum balance
                if (storedSavings.get().getBalance().getAmount().compareTo(storedSavings.get().getMinimumBalance().getAmount()) == -1) {
                    //Subtract penalty fee to balance
                    storedSavings.get().getBalance().decreaseAmount(storedSavings.get().getPenaltyFee());

                    savingsRepository.save(storedSavings.get());
                }
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }


    @Override
    public void applyInterest(Integer id) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);

        if (storedSavings.isPresent()) {
            while(LocalDate.now().isAfter(storedSavings.get().getLastInterestApplication().plusYears(1))){

                //Adds 1 year to last interest application
                storedSavings.get().setLastInterestApplication(storedSavings.get().getLastInterestApplication().plusYears(1));

                //Adds interest to balance
                Money currentBalance = storedSavings.get().getBalance();
                BigDecimal interestRate = storedSavings.get().getInterestRate();
                BigDecimal interest = currentBalance.getAmount().multiply(interestRate);

                storedSavings.get().setBalance(new Money(currentBalance.increaseAmount(interest)));


                savingsRepository.save(storedSavings.get());
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public Money checkBalance(Integer id) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);

        if (storedSavings.isPresent()) {

            //Applies penalty fee and interest if applicable to update balance
            applyPenaltyFee(id);
            applyInterest(id);
            savingsRepository.save(storedSavings.get());

            return storedSavings.get().getBalance();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
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
        applyInterest(originId);

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
