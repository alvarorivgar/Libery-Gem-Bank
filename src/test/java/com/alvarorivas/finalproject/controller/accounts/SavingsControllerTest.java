package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.CreditCard;
import com.alvarorivas.finalproject.model.accounts.Savings;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.CreditCardRepository;
import com.alvarorivas.finalproject.repository.accounts.SavingsRepository;
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
class SavingsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    SavingsRepository savingsRepository;

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

        Savings savings = new Savings(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, new BigDecimal(0.0025), "1234",
                new Money(new BigDecimal(200)));

        Savings savings2 = new Savings(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, new BigDecimal(0.0025), "1234",
                new Money(new BigDecimal(200)));

        accountHolderRepository.save(accountHolder);
        savingsRepository.saveAll(List.of(savings, savings2));
    }

    @AfterEach
    void tearDown() {

        savingsRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findById() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/savings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Savings savings = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Savings.class);

        assertEquals("Pikachu", savings.getPrimaryOwner().getName());
        assertEquals(new Money(new BigDecimal(1000)), savings.getBalance());
    }

    @Test
    void createAccount() throws Exception{

        AccountHolder accountHolder = new AccountHolder("Sauron", LocalDate.of(1990, 05, 14),
                new Address("Mordor"), null);
        accountHolderRepository.save(accountHolder);

        Savings newSavings = new Savings(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, new BigDecimal(0.0025), "1234",
                new Money(new BigDecimal(200)));

        String payload = objectMapper.writeValueAsString(newSavings);
        MvcResult mvcResult = mockMvc.perform(post("/savings")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Savings savings = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Savings.class);

        assertEquals(newSavings.getSecretKey(), savings.getSecretKey());
        assertEquals(newSavings.getPrimaryOwner().getName(), savings.getPrimaryOwner().getName());
        assertTrue(savingsRepository.findById(1).isPresent());
    }

    @Test
    void updateBalance() throws Exception{

        Money money = new Money(new BigDecimal(500));

        String payload = objectMapper.writeValueAsString(money);
        MvcResult mvcResult = mockMvc.perform(patch("/savings/1/update-balance")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Savings savings = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Savings.class);

        assertEquals(money, savings.getBalance());
    }

    @Test
    void updateAccount() throws Exception{

        Savings newSavings = new Savings(new Money(new BigDecimal(1000)), accountHolderRepository.findById(1).get(), null, Status.ACTIVE,
                new BigDecimal(0.0025), "1234", new Money(new BigDecimal(200)));

        String payload = objectMapper.writeValueAsString(newSavings);
        MvcResult mvcResult = mockMvc.perform(put("/savings/1/update-account")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Savings savings = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Savings.class);

        assertEquals(newSavings.getBalance(), savings.getBalance());
        assertEquals(newSavings.getStatus(), savings.getStatus());
    }

    @Test
    void deleteAccount() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/savings/1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertFalse(savingsRepository.findById(1).isPresent());
    }

    @Test
    void checkBalance() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/savings/1/balance")
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

        MvcResult mvcResult = mockMvc.perform(put("/savings/1/transfer?receiverName=Pikachu&receiverId=2")
                        .content(amount)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(new Money(new BigDecimal(500)), savingsRepository.findById(1).get().getBalance());
        assertEquals(new Money(new BigDecimal(1500)), savingsRepository.findById(2).get().getBalance());
    }
}