package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class CreditCard extends Account{

    @Embedded
    @NotNull
    private Money creditLimit;

    @NotNull
    private BigDecimal interestRate;

    public CreditCard() {
        super();
    }

    //Constructor without secondary owner
    public CreditCard(Money balance, AccountHolder primaryOwner, Money penaltyFee, Date creationDate, Status status,
                      Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, penaltyFee, creationDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    //Constructor with secondary owner
    public CreditCard(Integer accountId, Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money penaltyFee, Date creationDate,
                      Status status, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
