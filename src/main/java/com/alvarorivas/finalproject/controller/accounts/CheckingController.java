package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.service.accounts.CheckingService;
import com.alvarorivas.finalproject.service.accounts.StudentCheckingService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingService;

    @Autowired
    StudentCheckingService studentCheckingService;


    @GetMapping("/checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Checking findById(@PathVariable Integer id){
        return checkingService.findById(id).get();
    }

    @PostMapping("/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Checking checking){

       return null;
    }

    @PatchMapping("/checking/{id}/update-balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Checking updateBalance(@PathVariable Integer id, @RequestBody @Valid Money balance){

        return  null;
    }

}
