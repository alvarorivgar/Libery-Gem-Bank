package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.ThirdParty;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public interface ThirdPartyService {

    Optional<ThirdParty> findById(Integer id);

    ThirdParty createThirdParty(ThirdParty thirdParty);

    ThirdParty updateThirdParty(ThirdParty thirdParty);

    void deleteThirdParty(Integer id);
}
