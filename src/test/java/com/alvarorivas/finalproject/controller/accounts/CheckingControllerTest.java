package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    CheckingRepository checkingRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();


    }

    @AfterEach
    void tearDown() {

        checkingRepository.deleteAll();
    }

    @Test
    void createAccount() throws Exception {

        Checking newChecking = new Checking(new Money(new BigDecimal(500)), new AccountHolder("Perico", LocalDate.now().minusYears(20),
                new Address("Su casa"), null), null, Status.ACTIVE, "1234");

        String payload = objectMapper.writeValueAsString(newChecking);
        MvcResult mvcResult = mockMvc.perform(post("/checking")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Checking checking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Checking.class);

        assertEquals(newChecking.getAccountId(), checking.getAccountId());
        assertEquals(newChecking.getBalance(), checking.getBalance());
        assertEquals(newChecking.getSecretKey(), checking.getSecretKey());
    }

    @Test
    void updateBalance() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void checkBalance() {
    }

    @Test
    void transferMoney() {
    }
}