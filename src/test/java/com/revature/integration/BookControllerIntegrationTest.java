package com.revature.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.VirtualPublicLibraryApplication;
import com.revature.models.Book;
import com.revature.repo.BookRepo;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes= VirtualPublicLibraryApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")

public class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepo br;

    @BeforeEach
    public void resetDatabase() { br.deleteAll(); }

    private ObjectMapper om = new ObjectMapper();

    // tests for getBookByIsbn ------------------------------------------------

    @Test
    @Transactional
    public void testGetBookByIsbnSuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("isbn", "9781848820319");

        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        br.save(book);

        mockMvc.perform( get("/book/get-books-by-isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(registerBody))
        )
                .andDo(print())
                .andExpect(status().isAccepted());

        Book getBook = br.findBookByIsbn(9781848820319l);
        assertEquals("Principles of Programming Languages", getBook.getTitle());
        assertEquals("Ian Mackie", getBook.getAuthor());
        assertEquals(3, getBook.getGenreId());
        assertEquals(9781848820319l, getBook.getIsbn());
        assertEquals("Programming teaching basic", getBook.getSummary());
        assertEquals(2014, getBook.getYearPublished());
    }

    // tests for get by title -------------------------------------------------

    @Test
    @Transactional
    public void testGetByTitleSuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("title", "Principles of Programming Languages");

        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        br.save(book);

        mockMvc.perform( get("/book/get-books-by-title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isAccepted());

        Book getBook = br.findBookByTitle("Principles of Programming Languages");
        assertEquals("Principles of Programming Languages", getBook.getTitle());
        assertEquals("Ian Mackie", getBook.getAuthor());
        assertEquals(3, getBook.getGenreId());
        assertEquals(9781848820319l, getBook.getIsbn());
        assertEquals("Programming teaching basic", getBook.getSummary());
        assertEquals(2014, getBook.getYearPublished());
    }

    // tests for get by author ------------------------------------------------

    @Test
    @Transactional
    public void testGetByAuthorSuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("author", "Ian Mackie");

        Book book1 = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        Book book2 = new Book("BookTitle2", "Author2", 2, "Summary2", 9781848820414l, 2016);
        br.save(book1);
        br.save(book2);

        mockMvc.perform( get("/book/get-books-by-author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isAccepted());

        List<Book> getBooks = br.findAllBooksByAuthor("Ian Mackie");
        assertEquals(1, getBooks.size());
        Book getBook = getBooks.get(0);
        assertEquals("Principles of Programming Languages", getBook.getTitle());
        assertEquals("Ian Mackie", getBook.getAuthor());
        assertEquals(3, getBook.getGenreId());
        assertEquals(9781848820319l, getBook.getIsbn());
        assertEquals("Programming teaching basic", getBook.getSummary());
        assertEquals(2014, getBook.getYearPublished());

    }

    // test for 

}
