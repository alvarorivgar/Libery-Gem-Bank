package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.accounts.Checking;
import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.users.ThirdParty;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.model.util.Money;
import com.alvarorivas.finalproject.model.util.Status;
import com.alvarorivas.finalproject.repository.accounts.CheckingRepository;
import com.alvarorivas.finalproject.repository.users.AccountHolderRepository;
import com.alvarorivas.finalproject.repository.users.ThirdPartyRepository;
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
class ThirdPartyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    CheckingRepository checkingRepository;

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

        ThirdParty thirdParty1 = new ThirdParty("ABC", "PcComponentes");

        AccountHolder accountHolder1 = new AccountHolder("Perico", LocalDate.of(1992, 8, 26),
                new Address("Su casa"), null);

        Checking checking1 = new Checking(new Money(new BigDecimal(500)), accountHolder1, null, Status.ACTIVE, "1234");

        accountHolderRepository.save(accountHolder1);
        checkingRepository.save(checking1);
        thirdPartyRepository.save(thirdParty1);
    }

    @AfterEach
    void tearDown() {

        thirdPartyRepository.deleteAll();
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findById() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/third-party/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ThirdParty thirdParty = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ThirdParty.class);

        assertEquals("PcComponentes", thirdParty.getName());
        assertEquals("ABC", thirdParty.getHashedKey());
    }

    @Test
    void createThirdParty() throws Exception{

        ThirdParty newThirdParty = new ThirdParty("123", "InstantGaming");

        String payload = objectMapper.writeValueAsString(newThirdParty);
        MvcResult mvcResult = mockMvc.perform(post("/third-party")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ThirdParty thirdParty = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ThirdParty.class);

        assertEquals(newThirdParty.getName(), thirdParty.getName());
        assertTrue(thirdPartyRepository.findById(thirdParty.getId()).isPresent());
    }

    @Test
    void updateThirdParty() throws Exception{

        ThirdParty thirdPartyUpdate = new ThirdParty("321", "G2A");

        String payload = objectMapper.writeValueAsString(thirdPartyUpdate);
        MvcResult mvcResult = mockMvc.perform(put("/third-party/1/update")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ThirdParty thirdParty = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ThirdParty.class);

        assertEquals("321", thirdParty.getHashedKey());
        assertEquals("G2A", thirdParty.getName());
    }

    @Test
    void deleteThirdParty() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/third-party/1/delete"))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(thirdPartyRepository.findById(1).isPresent());
    }

    @Test
    void sendMoney() throws Exception{


        String amount = objectMapper.writeValueAsString(new Money(new BigDecimal(50)));

        MvcResult mvcResult = mockMvc.perform(put("/third-party/ABC/send?receiverId=1&secretKey=1234")
                        .content(amount)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(new Money(new BigDecimal(550)), checkingRepository.findById(1).get().getBalance());
    }

    @Test
    void receiveMoney() throws Exception{

        String amount = objectMapper.writeValueAsString(new Money(new BigDecimal(50)));

        MvcResult mvcResult = mockMvc.perform(put("/third-party/ABC/charge?receiverId=1&secretKey=1234")
                        .content(amount)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(new Money(new BigDecimal(450)), checkingRepository.findById(1).get().getBalance());
    }
}