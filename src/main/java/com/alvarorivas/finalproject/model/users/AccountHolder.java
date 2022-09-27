package com.alvarorivas.finalproject.model.users;

import com.alvarorivas.finalproject.model.accounts.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account_holder")
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer accountHolderId;

    @NotNull
    private String name;

    @NotNull
    private Date birthDate;

    @Embedded
    @NotNull
    private Address primaryAddress;

    @Embedded
    @NotNull
    private Address mailingAddress;

    public AccountHolder() {
    }

    //Constructor without mailing address
    public AccountHolder(Integer accountHolderId, String name, Date birthDate, Address primaryAddress) {
        this.accountHolderId = accountHolderId;
        this.name = name;
        this.birthDate = birthDate;
        this.primaryAddress = primaryAddress;
    }

    //Constructor with mailing address
    public AccountHolder(Integer accountHolderId, String name, Date birthDate, Address primaryAddress, Address mailingAddress) {
        this.accountHolderId = accountHolderId;
        this.name = name;
        this.birthDate = birthDate;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public Integer getAccountHolderId() {
        return accountHolderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
