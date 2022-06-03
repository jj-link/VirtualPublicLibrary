package com.revature.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.VirtualPublicLibraryApplication;
import com.revature.models.Book;
import com.revature.models.User;
import com.revature.repo.BookRepo;
import com.revature.repo.UserRepo;
import org.junit.Ignore;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

    @Autowired
    private BookRepo br;

    @BeforeEach
    public void resetDatabase() {
        ur.deleteAll();
        br.deleteAll(); }

    @BeforeEach
    public void resetBookDatabase() { br.deleteAll(); }

    private ObjectMapper om = new ObjectMapper();

    // tests for registerNewUser -------------------------------------------------

    /*
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
     */

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
        registerBody.put("email", "updatetest@mail.com");
        registerBody.put("password", "updatePassword");
        registerBody.put("firstName", "updateFirstname");
        registerBody.put("lastName", "updateLastname");

        User testUser = new User("test@email.com", "password", "firstName", "lastname");
        User user = ur.save(testUser);

        registerBody.put("userId", "" +user.getUserId());

        mockMvc.perform(post("/user/edit")
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


    @Test
    @Transactional
    public void testUpdateUserUnsuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();

        registerBody.put("email", "updatetest@mail.com");
        registerBody.put("password", "updatePassword");
        registerBody.put("firstName", "updateFirstname");
        registerBody.put("lastName", "updateLastname");

        registerBody.put("userId", "" + 2);
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

        User testUser = new User("test@email.com", "password", "firstName", "lastname");

        User expectUser = ur.save(testUser);
        int expectId = expectUser.getUserId();
        int expectUserRole = expectUser.getUserRole();

        registerBody.put("userId", "" + expectId);
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


    // tests for getAllUsers --------------------------------------------------

    @Test
    @Transactional
    public void testGetAllUsersSuccessful() throws Exception {

        User u1 = new User("tuser1@mail.com", "password", "Test", "One");
        User u2 = new User("tuser@mail.com", "password", "Test", "Two");

        ur.save(u1);
        ur.save(u2);

        String result = mockMvc.perform(get("/user/all-users")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<User> users = om.readValue(result, new TypeReference<List<User>>() { });

        System.out.println("Getting all users");

        for (User u : users) System.out.println(u.toString());
    }

    // tests for checkOutBook -------------------------------------------------


    @Test
    @Transactional
    public void testCheckOutBookSuccessful() throws Exception {
        LinkedHashMap<String, String> body = new LinkedHashMap<>();
        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        Book expectBook = br.save(book);
        long expectBookIsbn = expectBook.getIsbn();

        User u = new User("tuser@mail.com", "password", "Test", "User");
        User testUser = ur.save(u);

        body.put("userId", "" + testUser.getUserId());
        body.put("isbn", "" + expectBookIsbn);

        String result = mockMvc.perform(post("/user/checkout-book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(1, testUser.getCheckedOut().size());

        //assertEquals(1, books);
    }



    @Test
    @Transactional
    public void testUserCheckOutUnsuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();

        User testUser = new User("test@email.com", "password", "firstName", "lastname");

        User expectUser = ur.save(testUser);

        int expectId = expectUser.getUserId();

        registerBody.put("userId", "" + expectId);
        registerBody.put("isbn", "" + 9781848820319l);

        mockMvc.perform(post("/user/checkout-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isConflict());

    }
    // tests for getCheckedOutBooks -------------------------------------------

    @Test
    @Transactional
    public void testGetCheckedOutBooksSuccessful() throws Exception {
        User u = new User("tuser@mail.com", "password", "Test", "User");

        User testUser = ur.save(u);

        Book book1 = new Book(1, "Test Book 1", "Test Author 1", 1, "Test summary 1", 1, 1, 2014);
        Book book2 = new Book(2, "Test Book 2", "Test Author 2", 2, "Test summary 2", 2, 2, 2014);
        Book book3 = new Book(3, "Test Book 3", "Test Author 3", 3, "Test summary 3", 3, 3, 2014);
        List<Book> testBooks = new ArrayList<>();
        testBooks.add(book1);
        testBooks.add(book2);
        testBooks.add(book3);
        u.setCheckedOut(testBooks);

        br.save(book1);
        br.save(book2);
        br.save(book3);

        String result = mockMvc.perform(get("/user/checkout-show/" + testUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() { });

        System.out.println("Getting all checked out books");
        for (Book b : books) System.out.println(b.toString());
        assertEquals(3, books.size());
    }

    @Test
    @Transactional
    public void testGetCheckedOutBooksUnsuccessful() throws Exception {
        User u = new User("tuser@mail.com", "password", "Test", "User");

        User getUser = ur.save(u);

        u.setCheckedOut(new ArrayList<>());

        String result = mockMvc.perform(get("/user/checkout-show/" + getUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() { });
        assertEquals(0, books.size());
    }

}
