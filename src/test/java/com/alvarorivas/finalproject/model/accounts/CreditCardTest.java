package com.alvarorivas.finalproject.model.accounts;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    private CreditCard card1;

    private CreditCard card2;


    @BeforeEach
    void setUp() {

       // CreditCard card1 = new CreditCard(new Money(new BigDecimal(1000)), new AccountHolder("Perico", LocalDate.now().minusYears(20),
         //       new Address("Su casa"), null), null, Status.ACTIVE, new Money(new BigDecimal(100000)), null);

        CreditCard card2 = new CreditCard();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setCreditLimit() {

    }

    @Test
    void setInterestRate() {
    }
}