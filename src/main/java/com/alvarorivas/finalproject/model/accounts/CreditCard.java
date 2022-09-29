package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;

import javax.persistence.Embedded;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditCard extends Account{

    @Embedded
    @Max(value = 100000, message = "Credit limit cannot be higher than 100,000")
    private Money creditLimit;

    @DecimalMin(value = "0.1", message = "Interest rate cannot be lower than 0.1")
    private BigDecimal interestRate;

    public CreditCard() {
        super();
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Status status, Money creditLimit,
                        BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, status);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {

        if(creditLimit == null){
            creditLimit = new Money(new BigDecimal(100));
        }else {
            this.creditLimit = creditLimit;
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {

        if(interestRate == null){
            interestRate = new BigDecimal(0.2);
        }else {
            this.interestRate = interestRate;
        }
    }
}
