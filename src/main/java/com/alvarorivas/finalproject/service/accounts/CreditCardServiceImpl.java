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
                BigDecimal monthlyInterest = currentBalance.getAmount().multiply(interestRate.divide(new BigDecimal(12)));

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

        Optional<CreditCard> originAccount = creditCardRepository.findById(originId);

        Integer receiverAccount = accountTypeChecker(receiverId);


        if(!originAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");}

        if(receiverAccount == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");
        }

        //Check if balance is updated
        applyInterest(originId);


        //Check if amount to transfer is lower than origin account's current balance
        if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }


        originAccount.get().getBalance().decreaseAmount(amount);
        creditCardRepository.save(originAccount.get());

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
