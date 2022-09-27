package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer accountId;

    @Embedded
    @NotNull
    private Money balance;

    @OneToOne
    @JoinColumn(name = "account_holder_id")
    @NotNull
    private AccountHolder primaryOwner;

    @OneToOne
    @JoinColumn(name = "account_holder_id")
    private AccountHolder secondaryOwner;

    @Embedded
    @NotNull
    private Money penaltyFee;

    @NotNull
    private Date creationDate;

    @NotNull
    private Status status;


    public Account() {
    }

    //Constructor without secondary owner
    public Account(Money balance, AccountHolder primaryOwner, Money penaltyFee, Date creationDate, Status status) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = penaltyFee;
        this.creationDate = creationDate;
        this.status = status;
    }

    //Constructor with secondary owner
    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money penaltyFee, Date creationDate, Status status) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = penaltyFee;
        this.creationDate = creationDate;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
