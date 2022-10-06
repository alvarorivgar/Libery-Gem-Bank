package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

@MappedSuperclass
@AttributeOverrides({
        @AttributeOverride(name = "penaltyFee.amount", column = @Column(name = "penalty_fee_amount")),
        @AttributeOverride(name = "penaltyFee.currency", column = @Column(name = "penalty_fee_currency")),
        @AttributeOverride(name = "primaryOwner.accountHolderId", column = @Column(name = "primary_holder_id")),
        @AttributeOverride(name = "primaryOwner.primaryAddress", column = @Column(name = "primary_holder_primary_address")),
        @AttributeOverride(name = "secondaryOwner.accountHolderId", column = @Column(name = "secondary_holder_id")),
        @AttributeOverride(name = "secondaryOwner.primaryAddress", column = @Column(name = "secondary_holder_primary_address"))
})
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer accountId;

    @Embedded
    @NotNull
    private Money balance;

    @ManyToOne
    @JoinColumn(name = "primary_holder_id")
    @NotNull
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_holder_id")
    private AccountHolder secondaryOwner;

    @Embedded
    @NotNull
    private Money penaltyFee;

    @PastOrPresent
    private LocalDate creationDate;

    @NotNull
    private Status status;


    public Account() {
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Status status) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = new Money(new BigDecimal(40));
        this.creationDate = LocalDate.now();
        this.status = status;
    }
    public Integer getAccountId() {
        return accountId;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public Money getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(Money penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
