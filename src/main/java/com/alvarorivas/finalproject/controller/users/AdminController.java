package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.users.Admin;
import com.alvarorivas.finalproject.service.users.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Admin findById(@PathVariable Integer id){

        return adminService.findById(id).get();
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@RequestBody @Valid Admin admin){

        return adminService.createAdmin(admin);
    }

    @PatchMapping("/admin/{id}/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Admin updateAdmin(@PathVariable Integer id, @RequestBody @Valid Admin admin) {

        return adminService.updateAdmin(id, admin);
    }

    @DeleteMapping("/admin/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdmin(@PathVariable Integer id){

        adminService.deleteAdmin(id);
    }
}
