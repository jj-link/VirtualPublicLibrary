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

        registerBody.put("email", "tuser@mail.com");
        registerBody.put("password", "password");
        registerBody.put("firstName", "Test");
        registerBody.put("lastName", "User");


        ur.save(new User(registerBody.get("email"), registerBody.get("password"), registerBody.get("firstName"), registerBody.get("lastName")));

        // URL should same as the URL in controller
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isConflict());

    }

    // tests for loginUser -------------------------------------------------


    @Test
    @Transactional
    public void testLoginUserSuccessful() throws Exception{
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("email", "test@mail.com");
        registerBody.put("password", "password");

        // Need to store the temp user into mock database first
        ur.save(new User(registerBody.get("email"), registerBody.get("password")));

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(registerBody))
        )
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Transactional
    public void testLoginUserUnsuccessful() throws Exception{
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("email", "test@mail.com");
        registerBody.put("password", "password");

        // Did not save the temp user into mock database, getting the result as NULL

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    // Test for update User information -------------------------------------

    @Test
    @Transactional
    public void testUpdateUserSuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("userId", "3");
        registerBody.put("email", "updatetest@mail.com");
        registerBody.put("password", "updatePassword");
        registerBody.put("firstName", "updateFirstname");
        registerBody.put("lastName", "updateLastname");

        User testUser = new User("test@email.com", "password", "firstName", "lastname");
        testUser.setUserId(3);
        ur.save(testUser);


        mockMvc.perform( post("/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                        .andDo(print())
                        .andExpect(status().isAccepted())
                        .andExpect(jsonPath("$.email").value("updatetest@mail.com"))
                        .andExpect(jsonPath("$.password").value("updatePassword"))
                        .andExpect(jsonPath("$.firstName").value("updateFirstname"))
                        .andExpect(jsonPath("$.lastName").value("updateLastname"));

        User updatedUser = ur.findUserByEmail("updatetest@mail.com");

        assertEquals("updatetest@mail.com", updatedUser.getEmail());
        assertEquals("updatePassword", updatedUser.getPassword());
        assertEquals("updateFirstname", updatedUser.getFirstName());
        assertEquals("updateLastname", updatedUser.getLastName());

    }

/*
    @Test
    @Transactional
    public void testUpdateUserUnsuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("userId", "5");
        registerBody.put("email", "updatetest@mail.com");
        registerBody.put("password", "updatePassword");
        registerBody.put("firstName", "updateFirstname");
        registerBody.put("lastName", "updateLastname");


        User testUser = new User("test@email.com", "password", "firstName", "lastname");
        testUser.setUserId(2);
        ur.save(testUser);

        mockMvc.perform(post("/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isNotAcceptable());

    }

    // Test for view user info -------------------------------------

    @Test
    @Transactional
    public void testViewUserSuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("userId", "4");



        User testUser = new User("test@email.com", "password", "firstName", "lastname");
        testUser.setUserId(4);
        testUser.setUserRole(1);
        ur.save(testUser);


        mockMvc.perform(get("/user/view-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isAccepted());

    }

    @Test
    @Transactional
    public void testViewUserUnsuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("userId", "12");



        mockMvc.perform(get("/user/view-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isNotAcceptable());

    }

*/
}
