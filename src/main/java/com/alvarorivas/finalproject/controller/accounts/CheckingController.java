package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Account;
import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.service.accounts.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CheckingController {

    @Autowired
    CheckingService checkingService;

    @GetMapping("/checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Checking findById(@PathVariable Integer id){

        return checkingService.findById(id).get();
    }

    @PostMapping("/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Checking checking){

       return checkingService.createAccount(checking);
    }

    @PatchMapping("/checking/{id}/update-balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Checking updateBalance(@PathVariable Integer id, @RequestBody @Valid Money balance){

        return  checkingService.updateBalance(id, balance);
    }

    @PutMapping("/checking/{id}/update-account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Checking updateAccount(@PathVariable Integer id, @RequestBody @Valid Checking checking){

        return checkingService.updateAccount(id, checking);
    }

    @DeleteMapping("/checking/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Integer id){

        checkingService.deleteAccount(id);
    }

    @GetMapping("/checking/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money checkBalance(@PathVariable Integer id){

        return checkingService.checkBalance(id);
    }

    @PutMapping("/checking/{id}/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable(value = "id") Integer originId, @RequestParam String receiverName, @RequestParam @Valid Integer receiverId,
                              @RequestParam String accountType, @RequestBody Money amount){

        checkingService.transferMoney(originId, receiverName, receiverId, accountType, amount);
    }
}
