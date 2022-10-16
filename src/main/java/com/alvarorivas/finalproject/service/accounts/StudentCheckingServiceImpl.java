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

        Optional<StudentChecking> originAccount = studentCheckingRepository.findById(originId);

        Integer receiverAccount = accountTypeChecker(receiverId);


        if(!originAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin account not found");}

        if(receiverAccount == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found");
        }

        //Check if amount to transfer is lower than origin account's current balance
        if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }


        originAccount.get().getBalance().decreaseAmount(amount);
        studentCheckingRepository.save(originAccount.get());

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
