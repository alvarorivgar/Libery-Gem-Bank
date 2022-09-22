package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import java.math.BigDecimal;
import java.util.Date;

public class StudentChecking extends Account{

    private String secretKey;
    private BigDecimal penaltyFee;

    public StudentChecking() {
        super();
    }
    //Constructor without secondary owner
    public StudentChecking(Integer accountId, BigDecimal balance, AccountHolder primaryOwner, BigDecimal penaltyFee, Date creationDate, Status status, String secretKey, BigDecimal penaltyFee1) {
        super(accountId, balance, primaryOwner, penaltyFee, creationDate, status);
        this.secretKey = secretKey;
        this.penaltyFee = penaltyFee1;
    }

    //Constructor with secondary owner
    public StudentChecking(Integer accountId, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, Date creationDate, Status status, String secretKey, BigDecimal penaltyFee1) {
        super(accountId, balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.secretKey = secretKey;
        this.penaltyFee = penaltyFee1;
    }
}
