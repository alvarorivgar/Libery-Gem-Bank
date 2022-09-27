package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class StudentChecking extends Account{

    @NotNull
    private String secretKey;

    @Embedded
    @NotNull
    private Money penaltyFee;

    public StudentChecking() {
        super();
    }
    //Constructor without secondary owner
    public StudentChecking(Money balance, AccountHolder primaryOwner, Money penaltyFee, Date creationDate, Status status, String secretKey, Money penaltyFee1) {
        super(balance, primaryOwner, penaltyFee, creationDate, status);
        this.secretKey = secretKey;
        this.penaltyFee = penaltyFee1;
    }

    //Constructor with secondary owner
    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money penaltyFee, Date creationDate, Status status, String secretKey, Money penaltyFee1) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.secretKey = secretKey;
        this.penaltyFee = penaltyFee1;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public Money getPenaltyFee() {
        return penaltyFee;
    }

    @Override
    public void setPenaltyFee(Money penaltyFee) {
        this.penaltyFee = penaltyFee;
    }
}
