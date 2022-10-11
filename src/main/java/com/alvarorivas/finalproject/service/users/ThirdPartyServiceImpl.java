package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.accounts.CreditCard;
import com.alvarorivas.finalproject.model.accounts.Savings;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.users.ThirdParty;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
import com.alvarorivas.finalproject.repository.accounts.CreditCardRepository;
import com.alvarorivas.finalproject.repository.accounts.SavingsRepository;
import com.alvarorivas.finalproject.repository.accounts.StudentCheckingRepository;
import com.alvarorivas.finalproject.repository.users.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService{

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;


    @Override
    public Optional<ThirdParty> findById(Integer id) {
        return thirdPartyRepository.findById(id);
    }

    @Override
    public ThirdParty createThirdParty(ThirdParty thirdParty) {

        if(thirdPartyRepository.findById(thirdParty.getId()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        return thirdPartyRepository.save(thirdParty);
    }

    @Override
    public ThirdParty updateThirdParty(Integer id, ThirdParty thirdParty) {

        Optional<ThirdParty> storedThirdParty = thirdPartyRepository.findById(id);

        if (storedThirdParty.isPresent()) {

            storedThirdParty.get().setName(thirdParty.getName());
            storedThirdParty.get().setHashedKey(thirdParty.getHashedKey());

            return thirdPartyRepository.save(storedThirdParty.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
    }

    @Override
    public void deleteThirdParty(Integer id) {

        Optional<ThirdParty> storedThirdParty = thirdPartyRepository.findById(id);

        if (storedThirdParty.isPresent()) {
            thirdPartyRepository.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
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
    public void sendMoney(String hashedKey, Integer receiverId, String secretKey, Money amount) {

        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);

        Integer receiverAccount = accountTypeChecker(receiverId);


        if(!thirdParty.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");
        }

        if(receiverAccount == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");
        }

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

    @Override
    public void receiveMoney(String hashedKey, Integer receiverId, String secretKey, Money amount) {

        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);

        Integer receiverAccount = accountTypeChecker(receiverId);


        if(!thirdParty.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");
        }

        if(receiverAccount == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");
        }

        switch (receiverAccount) {
            case 1 -> {
                Checking receiverChecking = checkingRepository.findById(receiverId).get();
                receiverChecking.getBalance().decreaseAmount(amount);
                checkingRepository.save(receiverChecking);
            }
            case 2 -> {
                CreditCard receiverCard = creditCardRepository.findById(receiverId).get();
                receiverCard.getBalance().decreaseAmount(amount);
                creditCardRepository.save(receiverCard);
            }
            case 3 -> {
                Savings receiverSavings = savingsRepository.findById(receiverId).get();
                receiverSavings.getBalance().decreaseAmount(amount);
                savingsRepository.save(receiverSavings);
            }
            case 4 -> {
                StudentChecking receiverStudent = studentCheckingRepository.findById(receiverId).get();
                receiverStudent.getBalance().decreaseAmount(amount);
                studentCheckingRepository.save(receiverStudent);
            }
        }
    }
}
