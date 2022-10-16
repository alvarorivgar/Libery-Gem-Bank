package com.alvarorivas.finalproject.repository.users;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer> {
}
