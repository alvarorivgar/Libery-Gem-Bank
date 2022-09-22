package com.alvarorivas.finalproject.model.users;

import com.alvarorivas.finalproject.model.accounts.Account;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account_holder")
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountHolderId;
    private String name;
    private Date birthDate;

    private List<Account> accounts;

    @Embedded
    private Address primaryAddress;

    @Embedded
    private Address mailingAddress;

    public AccountHolder() {
    }

    //Constructor without mailing address
    public AccountHolder(Integer accountHolderId, String name, Date birthDate, List<Account> accounts, Address primaryAddress) {
        this.accountHolderId = accountHolderId;
        this.name = name;
        this.birthDate = birthDate;
        this.accounts = accounts;
        this.primaryAddress = primaryAddress;
    }

    //Constructor with mailing address
    public AccountHolder(Integer accountHolderId, String name, Date birthDate, List<Account> accounts, Address primaryAddress, Address mailingAddress) {
        this.accountHolderId = accountHolderId;
        this.name = name;
        this.birthDate = birthDate;
        this.accounts = accounts;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

}
