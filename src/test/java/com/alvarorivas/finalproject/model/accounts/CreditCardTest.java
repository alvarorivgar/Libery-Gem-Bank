package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;


class CreditCardTest {

    private CreditCard card1;

    @Test
    void invalidCreditLimitThrowsException() {

        CreditCard card1 = new CreditCard(new Money(new BigDecimal(500)), new AccountHolder("Perico", LocalDate.of(1992,8,26),
                new Address("Casa"), null), null, Status.FROZEN, new Money(new BigDecimal(500)), new BigDecimal(0.5));

        assertThrows(ResponseStatusException.class, ()-> card1.setCreditLimit(new Money(new BigDecimal(100001))));
    }

    @Test
    void invalidInterestRateThrowsException() {

        CreditCard card1 = new CreditCard(new Money(new BigDecimal(500)), new AccountHolder("Perico", LocalDate.of(1992,8,26),
                new Address("Casa"), null), null, Status.FROZEN, new Money(new BigDecimal(500)), new BigDecimal(0.5));

        assertThrows(ResponseStatusException.class, ()-> card1.setInterestRate(new BigDecimal(0.05)));
    }
}