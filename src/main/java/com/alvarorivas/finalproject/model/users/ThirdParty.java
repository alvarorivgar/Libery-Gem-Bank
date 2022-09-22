package com.alvarorivas.finalproject.model.users;

import com.alvarorivas.finalproject.model.accounts.Account;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "third_party")
public class ThirdParty {

    @Id
    private String hashedKey;
    private String name;

    private List<Account> accounts;

    public ThirdParty(String hashedKey, String name) {
        this.hashedKey = hashedKey;
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThirdParty() {
    }

    public ThirdParty(String hashedKey, String name, List<Account> accounts) {
        this.hashedKey = hashedKey;
        this.name = name;
        this.accounts = accounts;
    }
}
