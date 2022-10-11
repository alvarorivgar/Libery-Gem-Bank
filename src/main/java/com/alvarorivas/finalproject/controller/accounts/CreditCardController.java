package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.CreditCard;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.service.accounts.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CreditCardController {

    @Autowired
    CreditCardService creditCardService;

    @GetMapping("/credit-card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard findById(@PathVariable Integer id){

        return creditCardService.findById(id).get();
    }

    @PostMapping("/credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createAccount(@RequestBody @Valid CreditCard creditCard){

        return creditCardService.createAccount(creditCard);
    }

    @PatchMapping("/credit-card/{id}/update-balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CreditCard updateBalance(@PathVariable Integer id, @RequestBody @Valid Money balance){

        return creditCardService.updateBalance(id, balance);
    }

    @PutMapping("/credit-card/{id}/update-account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CreditCard updateAccount(@PathVariable Integer id, @RequestBody @Valid CreditCard creditCard){

        return creditCardService.updateAccount(id, creditCard);
    }

    @DeleteMapping("/credit-card/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Integer id){

        creditCardService.deleteAccount(id);
    }

    @GetMapping("/credit-card/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money checkBalance(@PathVariable Integer id){

        return creditCardService.checkBalance(id);
    }

    @PutMapping("/credit-card/{id}/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@PathVariable(value = "id") Integer originId, @RequestParam String receiverName, @RequestParam @Valid Integer receiverId,
                              @RequestBody Money amount){

        creditCardService.transferMoney(originId, receiverName, receiverId, amount);
    }
}
