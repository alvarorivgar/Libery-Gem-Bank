package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.service.accounts.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StudentCheckingController {

    @Autowired
    StudentCheckingService studentCheckingService;

    @GetMapping("/student-checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentChecking findById(@PathVariable Integer id){

        return studentCheckingService.findById(id).get();
    }

    @PatchMapping("/student-checking/{id}/update-balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StudentChecking updateBalance(@PathVariable Integer id, @RequestBody @Valid Money balance){

        return  studentCheckingService.updateBalance(id, balance);
    }

    @PutMapping("/student-checking/{id}/update-account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StudentChecking updateAccount(@PathVariable Integer id, @RequestBody @Valid StudentChecking studentChecking){

        return studentCheckingService.updateAccount(id, studentChecking);
    }

    @DeleteMapping("/student-checking/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Integer id){

        studentCheckingService.deleteAccount(id);
    }

    @GetMapping("/student-checking/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money checkBalance(@PathVariable Integer id){

        return studentCheckingService.checkBalance(id);
    }

    @PutMapping("/student-checking/{id}/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable(value = "id") Integer originId, @RequestParam String receiverName, @RequestParam @Valid Integer receiverId,
                              @RequestBody Money amount){

        studentCheckingService.transferMoney(originId, receiverName, receiverId, amount);
    }
}
