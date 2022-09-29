package com.alvarorivas.finalproject.repository.users;

import com.alvarorivas.finalproject.model.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
