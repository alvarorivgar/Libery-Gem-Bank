package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.CreditCard;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.CreditCardRepository;
import com.alvarorivas.finalproject.repository.users.AccountHolderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CreditCardControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        AccountHolder accountHolder = new AccountHolder("Pikachu", LocalDate.of(1990, 05, 14),
                new Address("Pokeball"), null);

        CreditCard creditCard = new CreditCard(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, new Money(new BigDecimal(500)),
                new BigDecimal(0.5));

        CreditCard creditCard2 = new CreditCard(new Money(new BigDecimal(500)), accountHolder, null, Status.ACTIVE, new Money(new BigDecimal(500)),
                new BigDecimal(0.5));

        accountHolderRepository.save(accountHolder);
        creditCardRepository.saveAll(List.of(creditCard, creditCard2));

    }

    @AfterEach
    void tearDown() {

        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findById() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/credit-card/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard creditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);

        assertEquals("Pikachu", creditCard.getPrimaryOwner().getName());
        assertEquals(new Money(new BigDecimal(1000)), creditCard.getBalance());
    }

    @Test
    void createAccount() throws Exception{

        AccountHolder accountHolder = new AccountHolder("Perico", LocalDate.of(1990, 05, 14),
                new Address("Su casa"), null);
        accountHolderRepository.save(accountHolder);

        CreditCard newCard = new CreditCard(new Money(new BigDecimal(500)), accountHolder, null, Status.ACTIVE, new Money(new BigDecimal(500)),
                new BigDecimal(0.5));

        String payload = objectMapper.writeValueAsString(newCard);
        MvcResult mvcResult = mockMvc.perform(post("/credit-card")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard creditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);

        assertEquals(newCard.getCreditLimit(), creditCard.getCreditLimit());
        assertEquals(newCard.getPrimaryOwner().getName(), creditCard.getPrimaryOwner().getName());
        assertTrue(creditCardRepository.findById(1).isPresent());
    }

    @Test
    void updateBalance() throws Exception{

        Money money = new Money(new BigDecimal(500));

        String payload = objectMapper.writeValueAsString(money);
        MvcResult mvcResult = mockMvc.perform(patch("/credit-card/1/update-balance")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard creditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);

        assertEquals(money, creditCard.getBalance());
    }

    @Test
    void updateAccount() throws Exception{

        CreditCard newCard = new CreditCard(new Money(new BigDecimal(500)), accountHolderRepository.findById(1).get(), null,
                Status.FROZEN, new Money(new BigDecimal(500)), new BigDecimal(0.5));

        String payload = objectMapper.writeValueAsString(newCard);
        MvcResult mvcResult = mockMvc.perform(put("/credit-card/1/update-account")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CreditCard creditCard = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditCard.class);

        assertEquals(newCard.getBalance(), creditCard.getBalance());
        assertEquals(newCard.getStatus(), creditCard.getStatus());
    }

    @Test
    void deleteAccount() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/credit-card/1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertFalse(creditCardRepository.findById(1).isPresent());
    }

    @Test
    void checkBalance() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/credit-card/1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Money balance = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Money.class);

        assertEquals(new Money(new BigDecimal(1000)), balance);
    }

    @Test
    void transferMoney() throws Exception{

        String amount = objectMapper.writeValueAsString(new Money(new BigDecimal(500)));

        MvcResult mvcResult = mockMvc.perform(put("/credit-card/1/transfer?receiverName=Pikachu&receiverId=2&accountType=creditcard")
                        .content(amount)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(new Money(new BigDecimal(500)), creditCardRepository.findById(1).get().getBalance());
        assertEquals(new Money(new BigDecimal(1000)), creditCardRepository.findById(2).get().getBalance());
    }
}