package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.Admin;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> findById(Integer id);

    Admin createAdmin(Admin admin);

    Admin updateAdmin(Integer id, Admin admin);

    void deleteAdmin(Integer id);
}