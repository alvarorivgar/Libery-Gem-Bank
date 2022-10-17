package com.alvarorivas.finalproject.controller.accounts;

import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

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

        Checking checking = new Checking(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, "1234");

        Checking checking2 = new Checking(new Money(new BigDecimal(1000)), accountHolder, null, Status.ACTIVE, "1234");


        accountHolderRepository.save(accountHolder);
        checkingRepository.save(checking);
        checkingRepository.save(checking2);


    }

    @AfterEach
    void tearDown() {

        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findbyId() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/checking/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Checking checking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Checking.class);

        assertEquals("Pikachu", checking.getPrimaryOwner().getName());
        assertEquals(new Money(new BigDecimal(1000)), checking.getBalance());
    }
    @Test
    void createAccount() throws Exception {

        AccountHolder accountHolder = new AccountHolder("Perico", LocalDate.of(1990, 05, 14),
                new Address("Su casa"), null);
        accountHolderRepository.save(accountHolder);

        Checking newChecking = new Checking(new Money(new BigDecimal(500)), accountHolder, null, Status.ACTIVE, "1234");

        String payload = objectMapper.writeValueAsString(newChecking);
        MvcResult mvcResult = mockMvc.perform(post("/checking")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Checking checking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Checking.class);

        assertEquals(newChecking.getSecretKey(), checking.getSecretKey());
        assertEquals(newChecking.getPrimaryOwner().getName(), checking.getPrimaryOwner().getName());
        assertTrue(checkingRepository.findById(1).isPresent());
    }

    @Test
    void createsStudentCheckingIfOwnerLessThan24() throws Exception {

        AccountHolder accountHolder = new AccountHolder("Perico", LocalDate.of(2001, 05, 14),
                new Address("Su casa"), null);
        accountHolderRepository.save(accountHolder);

        Checking newChecking = new Checking(new Money(new BigDecimal(500)), accountHolder, null, Status.ACTIVE, "1234");

        String payload = objectMapper.writeValueAsString(newChecking);
        MvcResult mvcResult = mockMvc.perform(post("/checking")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(studentCheckingRepository.findById(1).isPresent());
        assertFalse(checkingRepository.findById(1).isPresent());
    }

    @Test
    void updateBalance() throws Exception{

        Money money = new Money(new BigDecimal(500));

        String payload = objectMapper.writeValueAsString(money);
        MvcResult mvcResult = mockMvc.perform(patch("/checking/1/update-balance")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Checking checking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Checking.class);

        assertEquals(money, checking.getBalance());
    }

    @Test
    void updateAccount() throws Exception{

        Checking newChecking = new Checking(new Money(new BigDecimal(500)), accountHolderRepository.findById(1).get(), null, Status.FROZEN,
                "1234");

        String payload = objectMapper.writeValueAsString(newChecking);
        MvcResult mvcResult = mockMvc.perform(put("/checking/1/update-account")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Checking checking = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Checking.class);

        assertEquals(newChecking.getBalance(), checking.getBalance());
        assertEquals(newChecking.getStatus(), checking.getStatus());
    }

    @Test
    void deleteAccount() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/checking/1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertFalse(checkingRepository.findById(1).isPresent());

    }

    @Test
    void checkBalance() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/checking/1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Money balance = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Money.class);

        assertEquals(new Money(new BigDecimal(1000)), balance);
    }

    @Test
    void applyPenaltyFee() throws Exception{

        Checking checking = checkingRepository.findById(1).get();

        checking.setLastPenaltyFeeCheck(LocalDate.now().minusMonths(2));
        checking.setBalance(new Money(new BigDecimal(100)));

        checkingRepository.save(checking);


        MvcResult mvcResult = mockMvc.perform(get("/checking/1/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Money balance = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Money.class);

        assertEquals(new Money(new BigDecimal(60)), balance);
    }

    @Test
    void transferMoney() throws Exception{

        String amount = objectMapper.writeValueAsString(new Money(new BigDecimal(500)));

        MvcResult mvcResult = mockMvc.perform(put("/checking/1/transfer?receiverName=Pikachu&receiverId=2&accountType=checking")
                        .content(amount)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(new Money(new BigDecimal(500)), checkingRepository.findById(1).get().getBalance());
        assertEquals(new Money(new BigDecimal(1500)), checkingRepository.findById(2).get().getBalance());

    }
}