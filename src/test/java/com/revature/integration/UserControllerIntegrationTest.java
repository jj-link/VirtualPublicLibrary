package com.revature.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.VirtualPublicLibraryApplication;
import com.revature.models.User;
import com.revature.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes= VirtualPublicLibraryApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo ur;

    @BeforeEach
    public void resetDatabase() { ur.deleteAll(); }

    private ObjectMapper om = new ObjectMapper();

    // tests for registerNewUser -------------------------------------------------
    @Test
    @Transactional
    public void testRegisterNewUserSuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();

        registerBody.put("email", "tuser@mail.com");
        registerBody.put("password", "password");
        registerBody.put("firstName", "Test");
        registerBody.put("lastName", "User");

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(registerBody))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("tuser@mail.com"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));

        User registered = ur.findUserByEmail("tuser@mail.com");

        assertEquals("tuser@mail.com", registered.getEmail());
        assertEquals("password", registered.getPassword());
        assertEquals("Test", registered.getFirstName());
        assertEquals("User", registered.getLastName());

    }

    @Test
    @Transactional
    public void testRegisterNewUserUnsuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();

        registerBody.put("email", "test@email.com");
        registerBody.put("password", "Password");
        registerBody.put("firstName", "TestFirstName");
        registerBody.put("lastName", "TestLastName");

        ur.save(new User(registerBody.get("email"), registerBody.get("password"), registerBody.get("firstName"), registerBody.get("lastName")));

        mockMvc.perform(post("/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                // .andExpect(status().isConflict());
                .andExpect(status().isInternalServerError());

    }

    // tests for loginUser -------------------------------------------------



}
