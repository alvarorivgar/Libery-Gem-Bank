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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<ThirdParty> findById(Integer id) {
        return thirdPartyRepository.findById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ThirdParty createThirdParty(ThirdParty thirdParty) {

        return thirdPartyRepository.save(thirdParty);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteThirdParty(Integer id) {

        Optional<ThirdParty> storedThirdParty = thirdPartyRepository.findById(id);

        if (storedThirdParty.isPresent()) {
            thirdPartyRepository.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
    }

    @Override
    @PreAuthorize("hasRole('THIRD_PARTY')")
    public void sendMoney(String hashedKey, Integer receiverId, String secretKey, String accountType, Money amount) {

        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);

        if(!thirdParty.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");
        }

        //Check account type and send amount
        switch (accountType) {
            case "checking" -> {
                Checking receiverChecking = checkingRepository.findById(receiverId).get();

                if(receiverChecking == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                if(!secretKey.equals(receiverChecking.getSecretKey())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect secret key");
                }

                receiverChecking.getBalance().decreaseAmount(amount);
                checkingRepository.save(receiverChecking);
            }
            case "creditcard" -> {
                CreditCard receiverCard = creditCardRepository.findById(receiverId).get();

                if(receiverCard == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                receiverCard.getBalance().decreaseAmount(amount);
                creditCardRepository.save(receiverCard);
            }
            case "savings" -> {
                Savings receiverSavings = savingsRepository.findById(receiverId).get();

                if(receiverSavings == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                if(!secretKey.equals(receiverSavings.getSecretKey())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect secret key");
                }

                receiverSavings.getBalance().decreaseAmount(amount);
                savingsRepository.save(receiverSavings);
            }
            case "studentchecking" -> {
                StudentChecking receiverStudent = studentCheckingRepository.findById(receiverId).get();

                if(receiverStudent == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                if(!secretKey.equals(receiverStudent.getSecretKey())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect secret key");
                }

                receiverStudent.getBalance().decreaseAmount(amount);
                studentCheckingRepository.save(receiverStudent);
            }
        }
    }

    @Override
    @PreAuthorize("hasRole('THIRD_PARTY')")
    public void receiveMoney(String hashedKey, Integer receiverId, String secretKey, String accountType, Money amount) {

        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);


        if(!thirdParty.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");
        }

        //Check account type and charge amount
        switch (accountType) {
            case "checking" -> {
                Checking receiverChecking = checkingRepository.findById(receiverId).get();

                if(receiverChecking == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                if(!secretKey.equals(receiverChecking.getSecretKey())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect secret key");
                }

                receiverChecking.getBalance().decreaseAmount(amount);
                checkingRepository.save(receiverChecking);
            }
            case "creditcard" -> {
                CreditCard receiverCard = creditCardRepository.findById(receiverId).get();

                if(receiverCard == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                receiverCard.getBalance().decreaseAmount(amount);
                creditCardRepository.save(receiverCard);
            }
            case "savings" -> {
                Savings receiverSavings = savingsRepository.findById(receiverId).get();

                if(receiverSavings == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                if(!secretKey.equals(receiverSavings.getSecretKey())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect secret key");
                }

                receiverSavings.getBalance().decreaseAmount(amount);
                savingsRepository.save(receiverSavings);
            }
            case "studentchecking" -> {
                StudentChecking receiverStudent = studentCheckingRepository.findById(receiverId).get();

                if(receiverStudent == null){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found");}

                if(!secretKey.equals(receiverStudent.getSecretKey())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect secret key");
                }

                receiverStudent.getBalance().decreaseAmount(amount);
                studentCheckingRepository.save(receiverStudent);
            }
        }
    }
}
