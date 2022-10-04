package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;

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
    private BigDecimal interestRate;

    @NotBlank
    private String secretKey;

    @Embedded
    @NotNull
    @Min(value = 100, message = "Minimum balance cannot be lower than 100")
    private Money minimumBalance;

    private LocalDate lastMaintenanceFeeApplication;


    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   Status status, BigDecimal interestRate, String secretKey, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner, status);
        setInterestRate(interestRate);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        this.lastMaintenanceFeeApplication = LocalDate.now();
    }

    public Savings() {
        super();
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {

        if(interestRate == null){
            interestRate = new BigDecimal(0.0025);
        }else {
            this.interestRate = interestRate;
        }
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

        if(minimumBalance == null){
            minimumBalance = new Money(new BigDecimal(1000));
        }else {
            this.minimumBalance = minimumBalance;
        }
    }

    public LocalDate getLastMaintenanceFeeApplication() {
        return lastMaintenanceFeeApplication;
    }

    public void setLastMaintenanceFeeApplication(LocalDate lastMaintenanceFeeApplication) {
        this.lastMaintenanceFeeApplication = lastMaintenanceFeeApplication;
    }
}
