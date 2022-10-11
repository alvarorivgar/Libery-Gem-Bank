package com.alvarorivas.finalproject.repository.users;

import com.alvarorivas.finalproject.model.users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Integer> {

    Optional<ThirdParty> findByHashedKey(String hashedKey);
}
