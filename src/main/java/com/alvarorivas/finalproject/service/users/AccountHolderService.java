package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public interface AccountHolderService {

    Optional<AccountHolder> findById(Integer id);
    AccountHolder createAccHolder(AccountHolder accountHolder);

    AccountHolder updateAccHolder(AccountHolder accountHolder);

    void deleteAccHolder(Integer id);
}
