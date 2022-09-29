package com.alvarorivas.finalproject.service.accounts;

import com.alvarorivas.finalproject.model.accounts.StudentChecking;

import java.util.Optional;

public interface StudentCheckingService {

    Optional<StudentChecking> findById(Integer accountId);

    StudentChecking createAccount(StudentChecking studentChecking);

    StudentChecking updateAccount(StudentChecking studentChecking); //Admins should be able to access the balance for any account and to modify it.

    void deleteAccount(Integer accountId);

}