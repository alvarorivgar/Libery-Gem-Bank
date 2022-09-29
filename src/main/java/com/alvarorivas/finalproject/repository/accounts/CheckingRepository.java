package com.alvarorivas.finalproject.repository.accounts;

import com.alvarorivas.finalproject.model.accounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Integer> {


}
