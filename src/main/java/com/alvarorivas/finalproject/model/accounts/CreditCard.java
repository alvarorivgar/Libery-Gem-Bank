package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public class CreditCard extends Account{

    private Integer creditLimit;
    private BigDecimal interestRate;

    public CreditCard() {
        super();
    }

    //Constructor without secondary owner
    public CreditCard(Integer accountId, BigDecimal balance, AccountHolder primaryOwner, BigDecimal penaltyFee, Date creationDate, Status status,
                      Integer creditLimit, BigDecimal interestRate) {
        super(accountId, balance, primaryOwner, penaltyFee, creationDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    //Constructor with secondary owner
    public CreditCard(Integer accountId, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, Date creationDate,
                      Status status, Integer creditLimit, BigDecimal interestRate) {
        super(accountId, balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
