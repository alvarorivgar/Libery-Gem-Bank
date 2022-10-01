package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.service.accounts.CheckingService;
import com.alvarorivas.finalproject.service.accounts.StudentCheckingService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingService;

    @Autowired
    StudentCheckingService studentCheckingService;

    @PostMapping("/checking")
    public Account createAccount(Checking checking){

        if(checking.getPrimaryOwner().getBirthDate().isAfter(LocalDate.now().minusYears(24))){

            Gson gson = new Gson();
            String jsonString = gson.toJson(checking);

            StudentChecking studentChecking = gson.fromJson(jsonString, StudentChecking.class);

            return studentCheckingService.createAccount(studentChecking);
        }else {
            return checkingService.createAccount(checking);
        }
    }
}
