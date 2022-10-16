package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SavingsTest {

    Savings savings1;

    @Test
    void invalidInterestRateThrowsException() {

        Savings savings1 = new Savings(new Money(new BigDecimal(1000)), new AccountHolder("Perico", LocalDate.of(1992,8,26),
                new Address("Casa"), null), null, Status.ACTIVE, new BigDecimal(0.0025), "1234",
                new Money(new BigDecimal(200)));

        assertThrows(ResponseStatusException.class, ()-> savings1.setInterestRate(new BigDecimal(1)));
    }

    @Test
    void invalidMinimumBalanceThrowsException() {

        Savings savings1 = new Savings(new Money(new BigDecimal(1000)), new AccountHolder("Perico", LocalDate.of(1992,8,26),
                new Address("Casa"), null), null, Status.ACTIVE, new BigDecimal(0.0025), "1234",
                new Money(new BigDecimal(200)));

        assertThrows(ResponseStatusException.class, ()-> savings1.setMinimumBalance(new Money(new BigDecimal(50))));
    }
}