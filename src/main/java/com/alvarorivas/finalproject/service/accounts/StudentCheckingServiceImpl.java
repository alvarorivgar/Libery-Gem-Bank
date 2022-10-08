package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.repository.accounts.AccountRepository;
import com.alvarorivas.finalproject.repository.accounts.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class StudentCheckingServiceImpl implements StudentCheckingService{

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    AccountRepository accountRepository;


    @Override
    public Optional<StudentChecking> findById(Integer id) {

        return studentCheckingRepository.findById(id);
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
    public void transferMoney(Integer originId, String receiverName, Integer receiverId, Money amount) {

        Optional<StudentChecking> originAccount = studentCheckingRepository.findById(originId);
        Optional<Account> receiverAccount = accountRepository.findById(receiverId);

        if(originAccount.isPresent()){

            if(receiverAccount.isPresent()){
                //Check if amount to transfer is greater than origin account's current balance
                if (amount.getAmount().compareTo(originAccount.get().getBalance().getAmount()) == -1){


                    originAccount.get().getBalance().decreaseAmount(amount);
                    receiverAccount.get().getBalance().increaseAmount(amount);
                    studentCheckingRepository.save(originAccount.get());
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
