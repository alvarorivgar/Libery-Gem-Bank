package com.alvarorivas.finalproject.repository.accounts;

import com.alvarorivas.finalproject.model.accounts.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Integer> {
}
