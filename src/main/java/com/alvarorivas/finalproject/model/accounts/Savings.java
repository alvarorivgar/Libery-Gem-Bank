package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings")
@AttributeOverrides({
        @AttributeOverride(name = "minimumBalance.amount", column = @Column(name = "min_balance_amount")),
        @AttributeOverride(name = "minimumBalance.currency", column = @Column(name = "min_balance_currency")),
})
public class Savings extends Account{

    @NotNull
    @DecimalMax(value = "0.5", message = "Interest rate cannot be higher than 0.5")
    private BigDecimal interestRate = new BigDecimal(0.0025);

    @NotBlank
    private String secretKey;

    @Embedded
    @NotNull
    private Money minimumBalance = new Money(new BigDecimal(1000));

    private LocalDate lastInterestApplication = getCreationDate().withMonth(1).withDayOfMonth(1);

    private LocalDate lastFeeApplication = getCreationDate().plusMonths(1).withDayOfMonth(1);


    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   Status status, BigDecimal interestRate, String secretKey, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner, status);
        setInterestRate(interestRate);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
    }

    public Savings() {
        super();
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

        if(minimumBalance.getAmount().compareTo(new BigDecimal(100)) == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum balance cannot be lower than 100");
        }
        this.minimumBalance = minimumBalance;
    }

    public LocalDate getLastInterestApplication() {
        return lastInterestApplication;
    }

    public void setLastInterestApplication(LocalDate lastMaintenanceFeeApplication) {
        this.lastInterestApplication = lastMaintenanceFeeApplication;
    }

    public LocalDate getLastFeeApplication() {
        return lastFeeApplication;
    }

    public void setLastFeeApplication(LocalDate lastFeeApplication) {
        this.lastFeeApplication = lastFeeApplication;
    }
}
