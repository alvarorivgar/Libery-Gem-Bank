package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "checking")
public class Checking extends Account{

    @NotBlank
    private String secretKey;

    @Embedded
    @NotNull
    private Money minimumBalance;

    @Embedded
    @NotNull
    private Money monthlyMaintenanceFee;

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Status status, String secretKey) {
        super(balance, primaryOwner, secondaryOwner, status);
        this.secretKey = secretKey;
        this.minimumBalance = new Money(new BigDecimal(250));
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
    }

    public Checking() {
        super();
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

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}
