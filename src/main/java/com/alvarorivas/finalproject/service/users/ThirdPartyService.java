package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.ThirdParty;
import com.alvarorivas.finalproject.model.util.Money;

import java.util.Optional;

public interface ThirdPartyService {

    Optional<ThirdParty> findById(Integer id);

    ThirdParty createThirdParty(ThirdParty thirdParty);

    ThirdParty updateThirdParty(Integer id, ThirdParty thirdParty);

    void deleteThirdParty(Integer id);

    public Integer accountTypeChecker(Integer id);

    void sendMoney(String hashedKey, Integer receiverId, String secretKey, Money amount);

    void receiveMoney(String hashedKey, Integer receiverId, String secretKey, Money amount);
}
