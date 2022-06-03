package com.revature.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.VirtualPublicLibraryApplication;
import com.revature.models.Book;
import com.revature.repo.BookRepo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    // tests for createBook ---------------------------------------------------
    @Test
    @Transactional
    public void testCreateBookSuccessful() throws Exception {
        LinkedHashMap<String, String> body = new LinkedHashMap<>();

        body.put("title", "Test Book");
        body.put("author", "Test Author");
        body.put("genreId", "1");
        body.put("summary", "Test summary");
        body.put("isbn", "9781111111111");
        body.put("yearPublished", "1970");

        mockMvc.perform(post("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.genreId").value("1"))
                .andExpect(jsonPath("$.summary").value("Test summary"))
                .andExpect(jsonPath("$.isbn").value("9781111111111"))
                .andExpect(jsonPath("$.yearPublished").value("1970"));

        Book book = br.findBookByIsbn(9781111111111l);

        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(1, book.getGenreId());
        assertEquals("Test summary", book.getSummary());
        assertEquals(9781111111111l, book.getIsbn());
        assertEquals(1970, book.getYearPublished());
    }

    @Test
    @Transactional
    public void testCreateBookUnsuccessful() throws Exception {
        LinkedHashMap<String, String> body = new LinkedHashMap<>();

        body.put("title", "Test Book");
        body.put("author", "Test Author");
        body.put("genreId", "1");
        body.put("summary", "Test summary");
        body.put("isbn", "9781111111111");
        body.put("yearPublished", "1970");

        int genreId = Integer.parseInt(body.get("genreId"));
        long isbn = Long.parseLong(body.get("isbn"));
        int year = Integer.parseInt(body.get("yearPublished"));

        Book book = new Book(body.get("title"), body.get("author"), genreId, body.get("summary"), isbn, year);
        br.save(book);

        mockMvc.perform(post("/book/create/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isConflict());

    }

    // tests for updateBook ---------------------------------------------------

    @Test
    @Transactional
    public void testUpdateBookSuccessful() throws Exception {
        LinkedHashMap<String, String> body = new LinkedHashMap<>();

        body.put("title", "Test Title");
        body.put("author", "Test Author");
        body.put("genreId", "1");
        body.put("summary", "Test summary");
        body.put("isbn", "1");
        body.put("yearPublished", "2015");

        Book testBook = new Book("Title", "Author", 2, "Summary", 2, 2017);
        Book book = br.save(testBook);
        body.put("bookId", "" +book.getBookId());

        mockMvc.perform(put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body))
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.genreId").value(1))
                .andExpect(jsonPath("$.summary").value("Test summary"))
                .andExpect(jsonPath("$.isbn").value(1))
                .andExpect(jsonPath("$.yearPublished").value(2015));
    }

    @Test
    @Transactional
    public void testUpdateBookUnsuccessful() throws Exception {
        LinkedHashMap<String, String> registerBody = new LinkedHashMap<>();
        registerBody.put("title", "updateTitle");
        registerBody.put("author", "updateAuthor");
        registerBody.put("genreId", "1");
        registerBody.put("summary", "updateSummary");
        registerBody.put("isbn", "123456789");
        registerBody.put("yearPublished", "2012");

        registerBody.put("book_id", "" + 2);

        mockMvc.perform(put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(registerBody))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // tests for getAllBooks --------------------------------------------------

    @Test
    @Transactional
    public void testGetAllBooksSuccessful() throws Exception {
        Book book1 = new Book(1, "Test Book 1", "Test Author 1", 1, "Test summary 1", 1, 1, 2014);
        Book book2 = new Book(2, "Test Book 2", "Test Author 2", 2, "Test summary 2", 2, 2, 2014);
        Book book3 = new Book(3, "Test Book 3", "Test Author 3", 3, "Test summary 3", 3, 3, 2014);
        Book book4 = new Book(4, "Test Book 4", "Test Author 4", 4, "Test summary 4", 4, 4, 2014);
        Book book5 = new Book(5, "Test Book 5", "Test Author 5", 5, "Test summary 5", 5, 5, 2014);
        Book book6 = new Book(6, "Test Book 6", "Test Author 6", 6, "Test summary 6", 6, 6, 2014);
        Book book7 = new Book(7, "Test Book 7", "Test Author 6", 7, "Test summary 6", 7, 7, 2014);
        Book book8 = new Book(8, "Test Book 8", "Test Author 6", 8, "Test summary 6", 8, 8, 2014);
        Book book9 = new Book(9, "Test Book 9", "Test Author 6", 9, "Test summary 6", 9, 9, 2014);
        Book book10 = new Book(10, "Test Book 10", "Test Author 6", 10, "Test summary 6", 10, 10, 2014);
        Book book11 = new Book(11, "Test Book 11", "Test Author 6", 11, "Test summary 6", 11, 11, 2014);

        br.save(book1);
        br.save(book2);
        br.save(book3);
        br.save(book4);
        br.save(book5);
        br.save(book6);
        br.save(book7);
        br.save(book8);
        br.save(book9);
        br.save(book10);
        br.save(book11);

        String result = mockMvc.perform(get("/book/get-all-books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() {});

        System.out.println("Getting all books");

        for (Book book : books) System.out.println(book.toString());
    }

    @Test
    @Transactional
    public void testGetAllBooksUnsuccessful() throws Exception {
        String result = mockMvc.perform(get("/book/get-all-books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() {});

        assertEquals(0, books.size());
    }

    // tests for getBookByTitle -----------------------------------------------

    @Test
    @Transactional
    public void testGetBookByTitleSuccessful() throws Exception {
        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        br.save(book);

        // String title = book.getTitle().replaceAll(" ", "%20");

        mockMvc.perform( get("/book/get-books-by-title/" + book.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
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

    @Test
    @Transactional
    public void testGetBookByTitleUnsuccessful() throws Exception {
        Book book = new Book("Principles", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        br.save(book);

        mockMvc.perform( get("/book/get-books-by-title/Title")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // tests for getBookByAuthor ----------------------------------------------

    @Test
    @Transactional
    public void testGetBookByAuthorSuccessful() throws Exception {
        Book book1 = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        Book book2 = new Book("BookTitle2", "Author2", 2, "Summary2", 9781848820414l, 2016);
        br.save(book1);
        br.save(book2);

        mockMvc.perform( get("/book/get-books-by-author/" + book1.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON)
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

    @Test
    @Transactional
    public void testGetBookByAuthorUnsuccessful() throws Exception {
        Book book1 = new Book("Principles of Programming Languages", "Ian", 3, "Programming teaching basic",  9781848820319l, 2014);
        Book book2 = new Book("BookTitle2", "Author2", 2, "Summary2", 9781848820414l, 2016);
        br.save(book1);


        mockMvc.perform( get("/book/get-books-by-author/" + book2.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    // tests for getBooksByGenreId ---------------------------------------------

    @Test
    @Transactional
    public void testGetBooksByGenreIdSuccessful() throws Exception {


        Book book1 = new Book("Test Book 1", "Test Author 1", 1, "Test summary 1", 1, 2014);
        Book book2 = new Book("Test Book 2", "Test Author 2", 2, "Test summary 2", 2, 2014);
        Book book3 = new Book("Test Book 3", "Test Author 3", 3, "Test summary 3", 3, 2014);
        Book book4 = new Book("Test Book 4", "Test Author 4", 4, "Test summary 4", 4, 2014);
        Book book5 = new Book("Test Book 5", "Test Author 5", 5, "Test summary 5", 5, 2014);
        Book book6 = new Book("Test Book 6", "Test Author 6", 1, "Test summary 6", 6, 2014);

        br.save(book1);
        br.save(book2);
        br.save(book3);
        br.save(book4);
        br.save(book5);
        br.save(book6);

        mockMvc.perform( get("/book/get-books-by-genreId/" + book1.getGenreId())
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isAccepted());

        List<Book> books = br.findAllByGenreId(1);
        assertEquals(2, books.size());
    }

    @Test
    @Transactional
    public void getBooksByGenreIdUnsuccessful() throws Exception {

        Book book2 = new Book("Test Book 2", "Test Author 2", 2, "Test summary 2", 2, 2014);
        Book book3 = new Book("Test Book 3", "Test Author 3", 3, "Test summary 3", 3, 2014);
        Book book4 = new Book("Test Book 4", "Test Author 4", 4, "Test summary 4", 4, 2014);
        Book book5 = new Book("Test Book 5", "Test Author 5", 5, "Test summary 5", 5, 2014);

        br.save(book2);
        br.save(book3);
        br.save(book4);
        br.save(book5);

        mockMvc.perform( get("/book/get-books-by-genreId/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isAccepted());

        List<Book> books = br.findAllByGenreId(1);
        assertEquals(0, books.size());

    }

    // tests for getBookByIsbn ------------------------------------------------

    @Test
    @Transactional
    public void testGetBookByIsbnSuccessful() throws Exception {
        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        br.save(book);

        mockMvc.perform( get("/book/get-books-by-isbn/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

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

    @Test
    @Transactional
    public void testGetBookByIsbnUnsuccessful() throws Exception {
        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  0, 2014);

        mockMvc.perform( get("/book/get-books-by-isbn/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // tests for deleteBookByIsbn ---------------------------------------------

    @Test
    @Transactional
    public void testDeleteBookByIsbnSuccessful() throws Exception {
        LinkedHashMap<String, Long> body = new LinkedHashMap<>();
        body.put("isbn", 9781848820319l);

        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  9781848820319l, 2014);
        br.save(book);

        mockMvc.perform(delete("/book/remove-books-by-isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isAccepted());

        List<Book> books = br.findAll();
        assertEquals(0, books.size());
    }

    @Test
    @Transactional
    public void testDeleteBookByIsbnUnsuccessful() throws Exception {
        LinkedHashMap<String, Long> body = new LinkedHashMap<>();
        body.put("isbn", 9781848820319l);

        Book book = new Book("Principles of Programming Languages", "Ian Mackie", 3, "Programming teaching basic",  1, 2014);
        br.save(book);

        mockMvc.perform(delete("/book/remove-books-by-isbn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body))
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // tests for getMostPopularBooks ------------------------------------------

    @Test
    @Transactional
    public void testGetMostPopularBooksSuccessful() throws Exception {

        Book book1 = new Book(1, "Test Book 1", "Test Author 1", 1, "Test summary 1", 1, 1, 2014);
        Book book2 = new Book(2, "Test Book 2", "Test Author 2", 2, "Test summary 2", 2, 2, 2014);
        Book book3 = new Book(3, "Test Book 3", "Test Author 3", 3, "Test summary 3", 3, 3, 2014);
        Book book4 = new Book(4, "Test Book 4", "Test Author 4", 4, "Test summary 4", 4, 4, 2014);
        Book book5 = new Book(5, "Test Book 5", "Test Author 5", 5, "Test summary 5", 5, 5, 2014);
        Book book6 = new Book(6, "Test Book 6", "Test Author 6", 6, "Test summary 6", 6, 6, 2014);
        Book book7 = new Book(7, "Test Book 7", "Test Author 6", 7, "Test summary 6", 7, 7, 2014);
        Book book8 = new Book(8, "Test Book 8", "Test Author 6", 8, "Test summary 6", 8, 8, 2014);
        Book book9 = new Book(9, "Test Book 9", "Test Author 6", 9, "Test summary 6", 9, 9, 2014);
        Book book10 = new Book(10, "Test Book 10", "Test Author 6", 10, "Test summary 6", 10, 10, 2014);
        Book book11 = new Book(11, "Test Book 11", "Test Author 6", 11, "Test summary 6", 11, 11, 2014);

        br.save(book1);
        br.save(book2);
        br.save(book3);
        br.save(book4);
        br.save(book5);
        br.save(book6);
        br.save(book7);
        br.save(book8);
        br.save(book9);
        br.save(book10);
        br.save(book11);

        String result = mockMvc.perform(get("/book/get-books-most-popular")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() {});

        for (Book book : books) System.out.println(book.toString());

    }

    @Test
    @Transactional
    public void testGetMostPopularBooksUnsuccessful() throws Exception {
        String result = mockMvc.perform(get("/book/get-books-most-popular")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() {});

        assertEquals(0, books.size());
    }

    // tests for getRecentBooks -----------------------------------------------

    @Test
    @Transactional
    public void testGetRecentBooksSuccessful() throws Exception {
        Book book1 = new Book(1, "Test Book 1", "Test Author 1", 1, "Test summary 1", 1, 1, 2014);
        Book book2 = new Book(2, "Test Book 2", "Test Author 2", 2, "Test summary 2", 2, 2, 2014);
        Book book3 = new Book(3, "Test Book 3", "Test Author 3", 3, "Test summary 3", 3, 3, 2014);
        Book book4 = new Book(4, "Test Book 4", "Test Author 4", 4, "Test summary 4", 4, 4, 2014);
        Book book5 = new Book(5, "Test Book 5", "Test Author 5", 5, "Test summary 5", 5, 5, 2014);
        Book book6 = new Book(6, "Test Book 6", "Test Author 6", 6, "Test summary 6", 6, 6, 2014);
        Book book7 = new Book(7, "Test Book 7", "Test Author 6", 7, "Test summary 6", 7, 7, 2014);
        Book book8 = new Book(8, "Test Book 8", "Test Author 6", 8, "Test summary 6", 8, 8, 2014);
        Book book9 = new Book(9, "Test Book 9", "Test Author 6", 9, "Test summary 6", 9, 9, 2014);
        Book book10 = new Book(10, "Test Book 10", "Test Author 6", 10, "Test summary 6", 10, 10, 2014);
        Book book11 = new Book(11, "Test Book 11", "Test Author 6", 11, "Test summary 6", 11, 11, 2014);

        br.save(book1);
        br.save(book2);
        br.save(book3);
        br.save(book4);
        br.save(book5);
        br.save(book6);
        br.save(book7);
        br.save(book8);
        br.save(book9);
        br.save(book10);
        br.save(book11);

        String result = mockMvc.perform(get("/book/recent")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() { });

        for (Book book : books) System.out.println(book.toString());
    }

    @Test
    @Transactional
    public void testGetRecentBooksUnsuccessful() throws Exception {
        String result = mockMvc.perform(get("/book/recent")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Book> books = om.readValue(result, new TypeReference<List<Book>>() {});

        assertEquals(0, books.size());

    }

}
