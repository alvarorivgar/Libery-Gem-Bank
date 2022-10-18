package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credit_card")
@AttributeOverrides({
        @AttributeOverride(name = "creditLimit.amount", column = @Column(name = "credit_limit_amount")),
        @AttributeOverride(name = "creditLimit.currency", column = @Column(name = "credit_limit_currency"))
})
public class CreditCard extends Account{

    @Embedded
    private Money creditLimit = new Money(new BigDecimal(100));

    private BigDecimal interestRate = new BigDecimal(0.2);

    private LocalDate lastInterestApplication = getCreationDate().plusMonths(1).withDayOfMonth(1);

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

        if(creditLimit.getAmount().compareTo(new BigDecimal(100000)) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit limit cannot exceed 100,000");
        }
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {

        if(interestRate.compareTo(new BigDecimal(0.1)) == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interest rate cannot be lower than 0.1");
        }
        this.interestRate = interestRate;
    }

    public LocalDate getLastInterestApplication() {
        return lastInterestApplication;
    }

    public void setLastInterestApplication(LocalDate lastInterestApplication) {
        this.lastInterestApplication = lastInterestApplication;
    }
}
