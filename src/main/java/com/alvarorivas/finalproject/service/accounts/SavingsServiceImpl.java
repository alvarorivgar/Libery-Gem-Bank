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
            storedSavings.get().setLastFeeApplication(savings.getLastFeeApplication());
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

                //Adds 1 month to last penalty fee check
                storedSavings.get().setLastFeeApplication(storedSavings.get().getLastFeeApplication().plusMonths(1));

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
    public void transferMoney(Integer originId, String receiverName, Integer receiverId, String accountType, Money amount) {

        Optional<Savings> originAccount = savingsRepository.findById(originId);

        if(!originAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");}

        //Check if balance is updated
        applyPenaltyFee(originId);
        applyInterest(originId);

        //Check if amount to transfer is lower than origin account's current balance
        if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        //Decrease balance of origin account
        originAccount.get().getBalance().decreaseAmount(amount);
        savingsRepository.save(originAccount.get());

        //Check account type and increase balance of receiver account
        switch (accountType) {
            case "checking" -> {
                Optional<Checking> receiverChecking = checkingRepository.findById(receiverId);

                if(!receiverChecking.isPresent()){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");}

                receiverChecking.get().getBalance().increaseAmount(amount);
                checkingRepository.save(receiverChecking.get());
            }
            case "creditcard" -> {
                Optional<CreditCard> receiverCard = creditCardRepository.findById(receiverId);

                if(!receiverCard.isPresent()){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");}

                receiverCard.get().getBalance().increaseAmount(amount);
                creditCardRepository.save(receiverCard.get());
            }
            case "savings" -> {
                Optional<Savings> receiverSavings = savingsRepository.findById(receiverId);

                if(!receiverSavings.isPresent()){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");}

                receiverSavings.get().getBalance().increaseAmount(amount);
                savingsRepository.save(receiverSavings.get());
            }
            case "studentchecking" -> {
                Optional<StudentChecking> receiverStudent = studentCheckingRepository.findById(receiverId);

                if(!receiverStudent.isPresent()){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");}

                receiverStudent.get().getBalance().increaseAmount(amount);
                studentCheckingRepository.save(receiverStudent.get());
            }
        }
    }
}
