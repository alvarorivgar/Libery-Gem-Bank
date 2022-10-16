package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.users.ThirdParty;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.service.users.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @GetMapping("/third-party/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ThirdParty> findById(@PathVariable Integer id){

        return thirdPartyService.findById(id);
    }

    @PostMapping("/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody @Valid ThirdParty thirdParty){

        return thirdPartyService.createThirdParty(thirdParty);
    }

    @PutMapping("third-party/{id}/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ThirdParty updateThirdParty(@PathVariable Integer id, @RequestBody @Valid ThirdParty thirdParty){

        return thirdPartyService.updateThirdParty(id, thirdParty);
    }

    @DeleteMapping("/third-party/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteThirdParty(@PathVariable Integer id){

        thirdPartyService.deleteThirdParty(id);
    }

    @PutMapping("third-party/{hashedKey}/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMoney(@PathVariable String hashedKey, @RequestParam Integer receiverId, @RequestParam @Valid String secretKey, @RequestBody @Valid Money amount){

        thirdPartyService.sendMoney(hashedKey, receiverId, secretKey, amount);
    }

    @PutMapping("third-party/{hashedKey}/charge")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receiveMoney(@PathVariable String hashedKey, @RequestParam Integer receiverId, @RequestParam @Valid String secretKey, @RequestBody @Valid Money amount){

        thirdPartyService.receiveMoney(hashedKey, receiverId, secretKey, amount);
    }
}
