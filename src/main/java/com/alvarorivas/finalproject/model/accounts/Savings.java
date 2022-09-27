package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import javax.persistence.Embedded;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class Savings extends Account{

    @NotNull
    @DecimalMax("0.5")
    private BigDecimal interestRate;

    @NotNull
    private String secretKey;

    @Embedded
    @NotNull
    private Money minimumBalance;

    public Savings() {
    }

    //Constructor without secondary owner
    public Savings(Money balance, AccountHolder primaryOwner, Money penaltyFee, Date creationDate, Status status,
                   BigDecimal interestRate, String secretKey, Money minimumBalance) {
        super(balance, primaryOwner, penaltyFee, creationDate, status);
        this.interestRate = interestRate;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
    }

    //Constructor with secondary owner
    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money penaltyFee, Date creationDate,
                   Status status, BigDecimal interestRate, String secretKey, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
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

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
