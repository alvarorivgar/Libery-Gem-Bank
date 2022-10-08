package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "checking")
@AttributeOverrides({
        @AttributeOverride(name = "minimumBalance.amount", column = @Column(name = "min_balance_amount")),
        @AttributeOverride(name = "monthlyMaintenanceFee.amount", column = @Column(name = "monthly_maintenance_fee_amount")),
        @AttributeOverride(name = "minimumBalance.currency", column = @Column(name = "min_balance_currency")),
        @AttributeOverride(name = "monthlyMaintenanceFee.currency", column = @Column(name = "monthly_maintenance_fee_currency"))
})
public class Checking extends Account{

    @NotBlank
    private String secretKey;

    @Embedded
    @NotNull
    private Money minimumBalance;

    private LocalDate lastFeeApplication;

    @Embedded
    @NotNull
    private Money monthlyMaintenanceFee;

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Status status, String secretKey) {
        super(balance, primaryOwner, secondaryOwner, status);
        this.secretKey = secretKey;
        this.minimumBalance = new Money(new BigDecimal(250));
        this.lastFeeApplication = getCreationDate().plusMonths(1).withDayOfMonth(1);
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

    public LocalDate getLastFeeApplication() {
        return lastFeeApplication;
    }

    public void setLastFeeApplication(LocalDate lastPenaltyApplication) {
        this.lastFeeApplication = lastPenaltyApplication;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}
