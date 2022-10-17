package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Savings;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.service.accounts.SavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SavingsController {

    @Autowired
    SavingsService savingsService;

    @GetMapping("/savings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Savings findById(@PathVariable Integer id){

        return savingsService.findById(id).get();
    }

    @PostMapping("/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Savings createAccount(@RequestBody @Valid Savings savings){

        return savingsService.createAccount(savings);
    }

    @PatchMapping("/savings/{id}/update-balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Savings updateBalance(@PathVariable Integer id, @RequestBody @Valid Money balance){

        return  savingsService.updateBalance(id, balance);
    }

    @PutMapping("/savings/{id}/update-account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Savings updateAccount(@PathVariable Integer id, @RequestBody @Valid Savings savings){

        return savingsService.updateAccount(id, savings);
    }

    @DeleteMapping("/savings/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Integer id){

        savingsService.deleteAccount(id);
    }

    @GetMapping("/savings/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money checkBalance(@PathVariable Integer id){

        return savingsService.checkBalance(id);
    }

    @PutMapping("/savings/{id}/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable(value = "id") Integer originId, @RequestParam String receiverName, @RequestParam @Valid Integer receiverId,
                              @RequestParam String accountType, @RequestBody Money amount){

        savingsService.transferMoney(originId, receiverName, receiverId, accountType, amount);
    }
}
