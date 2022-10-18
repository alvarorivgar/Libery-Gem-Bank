package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.Admin;
import com.alvarorivas.finalproject.repository.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    AdminRepository adminRepository;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Admin> findById(Integer id) {
        return adminRepository.findById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Admin createAdmin(Admin admin) {

        return adminRepository.save(admin);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Admin updateAdmin(Integer id, Admin admin) {

        Optional<Admin> storedAdmin = adminRepository.findById(id);

        if(storedAdmin.isPresent()){

            storedAdmin.get().setName(admin.getName());
            return adminRepository.save(storedAdmin.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAdmin(Integer id) {

        Optional<Admin> storedAdmin = adminRepository.findById(id);

        if(storedAdmin.isPresent()){

            adminRepository.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
    }
}
