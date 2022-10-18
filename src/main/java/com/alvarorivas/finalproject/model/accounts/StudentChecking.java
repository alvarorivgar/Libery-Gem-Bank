package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "student_checking")

public class StudentChecking extends Account{

    private String secretKey;

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Status status, String secretKey) {
        super(balance, primaryOwner, secondaryOwner, status);
        this.secretKey = secretKey;
    }

    public StudentChecking() {
        super();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
