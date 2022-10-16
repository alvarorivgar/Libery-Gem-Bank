package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Savings;
import com.alvarorivas.finalproject.model.accounts.StudentChecking;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.StudentCheckingRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StudentCheckingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

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

        StudentChecking studentChecking = new StudentChecking(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, "1234");

        StudentChecking studentChecking2 = new StudentChecking(new Money(new BigDecimal(500)), accountHolder, null, Status.ACTIVE, "1234");

        accountHolderRepository.save(accountHolder);
        studentCheckingRepository.saveAll(List.of(studentChecking, studentChecking2));
    }

    @AfterEach
    void tearDown() {

        studentCheckingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findById() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/student-checking/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        StudentChecking studentChecking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentChecking.class);

        assertEquals("Pikachu", studentChecking.getPrimaryOwner().getName());
        assertEquals(new Money(new BigDecimal(1000)), studentChecking.getBalance());
    }

    @Test
    void updateBalance() throws Exception{

        Money money = new Money(new BigDecimal(500));

        String payload = objectMapper.writeValueAsString(money);
        MvcResult mvcResult = mockMvc.perform(patch("/student-checking/1/update-balance")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        StudentChecking studentChecking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentChecking.class);

        assertEquals(money, studentChecking.getBalance());
    }

    @Test
    void updateAccount() throws Exception{

        StudentChecking newStudentChecking = new StudentChecking(new Money(new BigDecimal(500)), accountHolderRepository.findById(1).get(),
                null, Status.FROZEN, "1234");

        String payload = objectMapper.writeValueAsString(newStudentChecking);
        MvcResult mvcResult = mockMvc.perform(put("/student-checking/1/update-account")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        StudentChecking studentChecking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentChecking.class);

        assertEquals(newStudentChecking.getBalance(), studentChecking.getBalance());
        assertEquals(newStudentChecking.getStatus(), studentChecking.getStatus());
    }

    @Test
    void deleteAccount() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/student-checking/1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertFalse(studentCheckingRepository.findById(1).isPresent());
    }

    @Test
    void checkBalance() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/student-checking/1/balance")
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

        MvcResult mvcResult = mockMvc.perform(put("/student-checking/1/transfer?receiverName=Pikachu&receiverId=2")
                        .content(amount)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(new Money(new BigDecimal(500)), studentCheckingRepository.findById(1).get().getBalance());
        assertEquals(new Money(new BigDecimal(1000)), studentCheckingRepository.findById(2).get().getBalance());
    }
}