package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        AccountHolder accountHolder1 = new AccountHolder("Samantha", LocalDate.of(1990, 8, 11), new Address("El bar de la esquina"),
                null);
        AccountHolder accountHolder2 = new AccountHolder("Agapito", LocalDate.of(1960, 01, 25), new Address("La casa de su tia"),
                new Address("La casa de su abuela"));

        accountHolderRepository.saveAll(List.of(accountHolder1,accountHolder2));
    }

    @AfterEach
    void tearDown() {

        accountHolderRepository.deleteAll();
    }

    @Test
    void findById() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/account-holder/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AccountHolder accountHolder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountHolder.class);

        assertEquals("Samantha", accountHolder.getName());
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
    void updateAccHolder() throws Exception{

        AccountHolder accHolderUpdate = new AccountHolder("Samuel", LocalDate.of(1990, 8, 11), new Address("El bar de la esquina"),
                null);

        String payload = objectMapper.writeValueAsString(accHolderUpdate);
        MvcResult mvcResult = mockMvc.perform(put("/account-holder/1/update")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AccountHolder accountHolder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountHolder.class);
        assertEquals("Samuel", accountHolder.getName());
    }

//    @Test
//    void invalidIdWhenUpdatingThrowsException() throws Exception{
//
//        AccountHolder accHolderUpdate = new AccountHolder("Samuel", LocalDate.of(1990, 8, 11), new Address("El bar de la esquina"),
//                null);
//
//        String payload = objectMapper.writeValueAsString(accHolderUpdate);
//        MvcResult mvcResult = mockMvc.perform(put("/account-holder/4/update")
//                        .content(payload)
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        //assertThrows();
//    }

    @Test
    void deleteAccHolder() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/account-holder/1/delete"))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(accountHolderRepository.findById(1).isPresent());
    }
}