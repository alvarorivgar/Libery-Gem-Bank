package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.repository.accounts.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentCheckingServiceImpl implements StudentCheckingService{

    @Autowired
    StudentCheckingRepository studentCheckingRepository;


    @Override
    public Optional<StudentChecking> findById(Integer accountId) {
        return studentCheckingRepository.findById(accountId);
    }

    @Override
    public StudentChecking createAccount(StudentChecking studentChecking) {
        return studentCheckingRepository.save(studentChecking);
    }

    @Override
    public StudentChecking updateAccount(StudentChecking studentChecking) {
        return studentCheckingRepository.save(studentChecking);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        studentCheckingRepository.deleteById(accountId);
    }
}
