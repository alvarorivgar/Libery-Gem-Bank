package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

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

    }

    @AfterEach
    void tearDown() {

        accountHolderRepository.deleteAll();
    }

    @Test
    void createAccHolder() throws Exception {

        AccountHolder newAccHolder = new AccountHolder("Perico", LocalDate.of(2002, 05, 26),
                new Address("Su casa"), null);

        String payload = objectMapper.writeValueAsString(newAccHolder);
        MvcResult mvcResult = mockMvc.perform(post("/account-holder")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AccountHolder accountHolder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountHolder.class);

        assertEquals(newAccHolder.getName(), accountHolder.getName());
        assertEquals(newAccHolder.getBirthDate(), accountHolder.getBirthDate());
        assertEquals(newAccHolder.getPrimaryAddress(), accountHolder.getPrimaryAddress());
        assertTrue(accountHolderRepository.findById(accountHolder.getAccountHolderId()).isPresent());
    }

    @Test
    void updateAccHolder() {
    }

    @Test
    void deleteAccHolder() {
    }
}