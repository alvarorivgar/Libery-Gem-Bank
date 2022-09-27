package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "checking")
public class Checking extends Account{

    @NotNull
    private String secretKey;

    @Embedded
    @NotNull
    private Money minimumBalance;

    @Embedded
    @NotNull
    private Money monthlyMaintenanceFee;


    //Constructor without secondary owner
    public Checking(Integer accountId, Money balance, AccountHolder primaryOwner, Money penaltyFee, Date creationDate, Status status, String secretKey,
                    Money minimumBalance, Money monthlyMaintenanceFee) {
        super(balance, primaryOwner, penaltyFee, creationDate, status);
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    //Constructor with secondary owner
    public Checking(Integer accountId, Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money penaltyFee, Date creationDate,
                    Status status, String secretKey, Money minimumBalance, Money monthlyMaintenanceFee) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
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
