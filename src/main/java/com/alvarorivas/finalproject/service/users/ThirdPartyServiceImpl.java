package com.alvarorivas.finalproject.service.users;

import com.alvarorivas.finalproject.model.users.ThirdParty;
import com.alvarorivas.finalproject.repository.users.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService{

    @Autowired
    ThirdPartyRepository thirdPartyRepository;


    @Override
    public Optional<ThirdParty> findById(Integer id) {
        return thirdPartyRepository.findById(id);
    }

    @Override
    public ThirdParty createThirdParty(ThirdParty thirdParty) {
        return thirdPartyRepository.save(thirdParty);
    }

    @Override
    public ThirdParty updateThirdParty(ThirdParty thirdParty) {
        return thirdPartyRepository.save(thirdParty);
    }

    @Override
    public void deleteThirdParty(Integer id) {
        thirdPartyRepository.deleteById(id);
    }
}
