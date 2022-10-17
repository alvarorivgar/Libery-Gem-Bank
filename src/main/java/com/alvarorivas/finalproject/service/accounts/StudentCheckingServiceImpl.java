package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.*;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class StudentCheckingServiceImpl implements StudentCheckingService{

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;


    @Override
    public Optional<StudentChecking> findById(Integer id) {

        return studentCheckingRepository.findById(id);
    }

    @Override
    public StudentChecking fromChecking(Checking checking) {

        StudentChecking studentChecking = new StudentChecking();

        studentChecking.setBalance(checking.getBalance());
        studentChecking.setPrimaryOwner(checking.getPrimaryOwner());
        studentChecking.setSecondaryOwner(checking.getSecondaryOwner());
        studentChecking.setStatus(checking.getStatus());
        studentChecking.setSecretKey(checking.getSecretKey());
        studentChecking.setCreationDate(checking.getCreationDate());
        studentChecking.setPenaltyFee(checking.getPenaltyFee());

        return studentCheckingRepository.save(studentChecking);
    }

    @Override
    public StudentChecking updateBalance(Integer id, Money balance) {

        Optional<StudentChecking> storedStudentChecking = studentCheckingRepository.findById(id);

        if (storedStudentChecking.isPresent()) {

            storedStudentChecking.get().setBalance(balance);
            return studentCheckingRepository.save(storedStudentChecking.get());

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public StudentChecking updateAccount(Integer id, StudentChecking studentChecking) {

        Optional<StudentChecking> storedStudentChecking = studentCheckingRepository.findById(id);

        if(storedStudentChecking.isPresent()){

            storedStudentChecking.get().setSecretKey(studentChecking.getSecretKey());
            storedStudentChecking.get().setBalance(studentChecking.getBalance());
            storedStudentChecking.get().setCreationDate(studentChecking.getCreationDate());
            storedStudentChecking.get().setStatus(studentChecking.getStatus());
            storedStudentChecking.get().setPenaltyFee(studentChecking.getPenaltyFee());
            storedStudentChecking.get().setPrimaryOwner(studentChecking.getPrimaryOwner());
            storedStudentChecking.get().setSecondaryOwner(studentChecking.getSecondaryOwner());

            return studentCheckingRepository.save(storedStudentChecking.get());
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }

    }

    @Override
    public void deleteAccount(Integer id) {

        Optional<StudentChecking> storedStudentChecking = studentCheckingRepository.findById(id);

        if (storedStudentChecking.isPresent()) {

            studentCheckingRepository.delete(storedStudentChecking.get());

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public Money checkBalance(Integer id) {

        Optional<StudentChecking> storedStudentChecking = studentCheckingRepository.findById(id);

        if (storedStudentChecking.isPresent()) {

            return storedStudentChecking.get().getBalance();

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account ID not found");
        }
    }

    @Override
    public void transferMoney(Integer originId, String receiverName, Integer receiverId, String accountType, Money amount) {

        Optional<StudentChecking> originAccount = studentCheckingRepository.findById(originId);

        if(!originAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");}

        //Check if amount to transfer is lower than origin account's current balance
        if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        //Decrease balance of origin account
        originAccount.get().getBalance().decreaseAmount(amount);
        studentCheckingRepository.save(originAccount.get());

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
