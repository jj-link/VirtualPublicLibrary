package com.revature.controllers;

import com.revature.models.Book;
import com.revature.services.BookService;
import com.revature.utils.LoggingUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;

@RestController
@CrossOrigin("*")
public class BookController {

    private BookService bs;

    @Autowired
    public BookController(BookService bs){
        this.bs = bs;
    }

    @PostMapping("/book/create")
    public ResponseEntity<Object> handleCreateBook(@RequestBody LinkedHashMap<String, String> body){
        try {
            int genreId = Integer.parseInt(body.get("genreId"));
            long isbn = Long.parseLong(body.get("isbn"));
            int yearPublished = Integer.parseInt(body.get("yearPublished"));
            Book book = bs.createBook(body.get("title"), body.get("author"), genreId, body.get("summary"), isbn, yearPublished);
            LoggingUtil.logger.info("Successfully created a new book: " + body.get("title"));
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Failed attempt to create a new book");
            return new ResponseEntity<>("Book already exists", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/book/get-all-books")
    public ResponseEntity<Object> handleGetAllBooks() {
        try {
            LoggingUtil.logger.info("Successfully retrieved all books");
            return new ResponseEntity<>(bs.getAllBooks(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve all books");
            return new ResponseEntity<>("Could not get all books", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/book/get-books-by-title/{title}")
    public ResponseEntity<Object> handleGetBooksByTitle(@PathVariable("title") String title) {
        try {
            title.replaceAll("%20", " ");
            LoggingUtil.logger.info("Successfully retrieved book(s) with title " + title);
            return new ResponseEntity<>(bs.getBookByTitle(title), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve book(s) with title " + title);
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/book/get-books-by-author/{author}")
    public ResponseEntity<Object> handleGetBooksByAuthor(@PathVariable("author") String author) {
        try {
            author.replaceAll("%20", " ");
            LoggingUtil.logger.info("Successfully retrieved all books from author " + author);
            return new ResponseEntity<>(bs.getBookByAuthor(author), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve all books from author " + author);
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/book/get-books-by-isbn/{isbn}")
    public ResponseEntity<Object> handleGetBooksByIsbn(@PathVariable("isbn") long isbn) {
        try {
            LoggingUtil.logger.info("Successfully retrieved book with ISBN " + isbn);
            return new ResponseEntity<>(bs.getBookByIsbn(isbn), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve book with ISBN " + isbn);
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/book/get-books-by-genreId/{genreId}")
    public ResponseEntity<Object> handleGetBooksByGenreId(@PathVariable("genreId") int genreId) {
        try {
            LoggingUtil.logger.info("Successfully retrieved all books with genre " + genreId);
            return new ResponseEntity<>(bs.getBookByGenreId(genreId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve all books with genre " + genreId);
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/remove-books-by-isbn")
    public ResponseEntity<Object> handleDeleteBooksByIsbn(@RequestBody LinkedHashMap<String, String> body) {
        try {
            long isbn = Long.parseLong(body.get("isbn"));
            bs.deleteBookByIsbn(isbn);
            String message = "Book with isbn " + isbn + " has been delete";
            LoggingUtil.logger.info("Successfully deleted book with ISBN " + isbn);
            return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not delete book with ISBN " + Long.parseLong(body.get("isbn")));
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/book/get-books-most-popular")
    public ResponseEntity<Object> handleGetBookPopular() {
        try {
            LoggingUtil.logger.info("Successfully retrieved most popular books");
            return new ResponseEntity<>(bs.getMostPopularBooks(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve most popular books");
            return new ResponseEntity<>("There is no book in library", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/update")
    public ResponseEntity<Object> handleUpdateBook(@RequestBody LinkedHashMap<String, String> body) {
        try {
            int id = Integer.parseInt(body.get("bookId"));
            int genreId = Integer.parseInt(body.get("genreId"));
            long isbn = Long.parseLong(body.get("isbn"));
            int yearPublished = Integer.parseInt(body.get("yearPublished"));
            Book book = bs.updateBook(id, body.get("title"), body.get("author"), genreId, body.get("summary"), isbn, yearPublished);
            LoggingUtil.logger.info("Successfully updated book with ID# " + id);
            return new ResponseEntity<>(book, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not update book with ID# " + Integer.parseInt(body.get("bookId")));
            return new ResponseEntity<>("Could not update book", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/book/recent")
    public ResponseEntity<Object> handleGetRecentBooks() {
        try {
            LoggingUtil.logger.info("Successfully retrieved recently added books");
            return new ResponseEntity<>(bs.getRecentBooks(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not retrieve recently added books");
            return new ResponseEntity<>("Could not find most recent books", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/book/search/{input}")
        public ResponseEntity<Object> handleGetBooksByGenreId(@PathVariable("input") String input) {
            try {
                LoggingUtil.logger.info("Successfully searched books with input " + input);
                return new ResponseEntity<>(bs.searchBooks(input), HttpStatus.ACCEPTED);
            } catch (Exception e) {
                e.printStackTrace();
                LoggingUtil.logger.info("Could not search for books with input " + input);
                return new ResponseEntity<>("Book Search Failed", HttpStatus.NOT_FOUND);
            }
        }
    }


