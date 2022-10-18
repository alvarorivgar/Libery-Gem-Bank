package com.alvarorivas.finalproject.controller.users;

import com.alvarorivas.finalproject.model.users.Admin;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Admin admin1 = new Admin("Pumba");

        adminRepository.save(admin1);
    }

    @AfterEach
    void tearDown() {

        adminRepository.deleteAll();
    }

    @Test
    void findById() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/admin/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Admin admin = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Admin.class);

        assertEquals("Pumba", admin.getName());
    }
    @Test
    void createAdmin() throws Exception{

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

        assertEquals(newAdmin.getName(), admin.getName());
        assertTrue(adminRepository.findById(admin.getId()).isPresent());
    }

    @Test
    void updateAdmin() throws Exception{

        Admin adminUpdate = new Admin("Timon");

        String payload = objectMapper.writeValueAsString(adminUpdate);
        MvcResult mvcResult = mockMvc.perform(patch("/admin/1/update")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Admin admin = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Admin.class);
        assertEquals("Timon", admin.getName());
    }

    @Test
    void deleteAdmin() throws Exception{

        MvcResult mvcResult = mockMvc.perform(delete("/admin/1/delete"))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(adminRepository.findById(1).isPresent());
    }
}