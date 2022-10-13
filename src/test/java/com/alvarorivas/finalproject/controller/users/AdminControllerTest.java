package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.users.AccountHolder;
import com.alvarorivas.finalproject.model.users.Admin;
import com.alvarorivas.finalproject.model.util.Address;
import com.alvarorivas.finalproject.repository.users.AccountHolderRepository;
import com.alvarorivas.finalproject.repository.users.AdminRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    AdminRepository adminRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {

        adminRepository.deleteAll();
    }

    @Test
    void createAccount() throws Exception{

        Admin newAdmin = new Admin("Juan Cuesta");

        String payload = objectMapper.writeValueAsString(newAdmin);
        MvcResult mvcResult = mockMvc.perform(post("/admin")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Admin admin = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Admin.class);

        assertEquals(newAdmin.getId(), admin.getId());
        assertEquals(newAdmin.getName(), admin.getName());
    }

    @Test
    void updateAccHolder() {
    }

    @Test
    void deleteAccHolder() {
    }
}