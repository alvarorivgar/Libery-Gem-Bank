package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.service.users.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;

    @GetMapping("/account-holder/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder findById(@PathVariable Integer id){

        return accountHolderService.findById(id).get();
    }

    @PostMapping("/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccount(@RequestBody @Valid AccountHolder accountHolder){

        return accountHolderService.createAccHolder(accountHolder);
    }

    @PutMapping("/account-holder/{id}/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AccountHolder updateAccHolder(@PathVariable Integer id, @RequestBody @Valid AccountHolder accountHolder) {

        return accountHolderService.updateAccHolder(id, accountHolder);
    }

    @DeleteMapping("/account-holder/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccHolder(@PathVariable Integer id){

        accountHolderService.deleteAccHolder(id);
    }
}
