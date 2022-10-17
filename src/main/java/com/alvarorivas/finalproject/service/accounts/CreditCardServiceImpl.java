package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.*;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CreditCardServiceImpl implements CreditCardService{

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Optional<CreditCard> findById(Integer accountId) {
        return creditCardRepository.findById(accountId);
    }

    @Override
    public CreditCard createAccount(CreditCard creditCard) {

        return creditCardRepository.save(creditCard);
    }

    @Override
    public CreditCard updateBalance(Integer id, Money balance) {

        Optional<CreditCard> storedCard = creditCardRepository.findById(id);

        if (storedCard.isPresent()) {

            storedCard.get().setBalance(balance);
            return creditCardRepository.save(storedCard.get());

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public CreditCard updateAccount(Integer id, CreditCard creditCard) {

        Optional<CreditCard> storedCard = creditCardRepository.findById(id);

        if (storedCard.isPresent()) {

            storedCard.get().setLastInterestApplication(creditCard.getLastInterestApplication());
            storedCard.get().setInterestRate(creditCard.getInterestRate());
            storedCard.get().setCreditLimit(creditCard.getCreditLimit());
            storedCard.get().setBalance(creditCard.getBalance());
            storedCard.get().setCreationDate(creditCard.getCreationDate());
            storedCard.get().setPenaltyFee(creditCard.getPenaltyFee());
            storedCard.get().setPrimaryOwner(creditCard.getPrimaryOwner());
            storedCard.get().setSecondaryOwner(creditCard.getSecondaryOwner());
            storedCard.get().setStatus(creditCard.getStatus());

            return creditCardRepository.save(storedCard.get());

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void deleteAccount(Integer id) {

        Optional<CreditCard> storedCard = creditCardRepository.findById(id);

        if (storedCard.isPresent()) {

            creditCardRepository.delete(storedCard.get());

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void applyInterest(Integer id) {

        Optional<CreditCard> storedCard = creditCardRepository.findById(id);

        if (storedCard.isPresent()) {
            while(LocalDate.now().isAfter(storedCard.get().getLastInterestApplication().plusMonths(1))){
                //Adds 1 month to last interest application
                storedCard.get().setLastInterestApplication(storedCard.get().getLastInterestApplication().plusMonths(1));
                //Adds interest to balance
                Money currentBalance = storedCard.get().getBalance();
                BigDecimal interestRate = storedCard.get().getInterestRate();
                BigDecimal monthlyInterest = currentBalance.getAmount().multiply(interestRate).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);

                storedCard.get().setBalance(new Money(currentBalance.increaseAmount(monthlyInterest)));


                creditCardRepository.save(storedCard.get());
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public Money checkBalance(Integer id) {

        Optional<CreditCard> storedCard = creditCardRepository.findById(id);

        if (storedCard.isPresent()) {

            //Applies interest if applicable to update balance
            applyInterest(id);
            creditCardRepository.save(storedCard.get());

            return storedCard.get().getBalance();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void transferMoney(Integer originId, String receiverName, Integer receiverId, String accountType, Money amount) {

        Optional<CreditCard> originAccount = creditCardRepository.findById(originId);

        if(!originAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");}

        //Check if balance is updated
        applyInterest(originId);


        //Check if amount to transfer is lower than origin account's current balance
        if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        //Decrease balance of origin account
        originAccount.get().getBalance().decreaseAmount(amount);
        creditCardRepository.save(originAccount.get());

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
