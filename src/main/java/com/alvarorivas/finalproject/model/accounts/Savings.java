package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public class Savings extends Account{

    private BigDecimal interestRate;
    private String secretKey;
    private BigDecimal minimumBalance;

    public Savings() {
    }

    //Constructor without secondary owner
    public Savings(Integer accountId, BigDecimal balance, AccountHolder primaryOwner, BigDecimal penaltyFee, Date creationDate, Status status,
                   BigDecimal interestRate, String secretKey, BigDecimal minimumBalance) {
        super(accountId, balance, primaryOwner, penaltyFee, creationDate, status);
        this.interestRate = interestRate;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
    }

    //Constructor with secondary owner
    public Savings(Integer accountId, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, Date creationDate,
                   Status status, BigDecimal interestRate, String secretKey, BigDecimal minimumBalance) {
        super(accountId, balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.interestRate = interestRate;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
