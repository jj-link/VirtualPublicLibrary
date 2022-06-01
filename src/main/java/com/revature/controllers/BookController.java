package com.revature.controllers;

import com.revature.models.Book;
import com.revature.services.BookService;
import com.revature.services.UserService;
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
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Book already exists", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/book/get-all-books")
    public ResponseEntity<Object> handleGetAllBooks() {
        try {
            return new ResponseEntity<>(bs.getAllBooks(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not get all books", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/book/get-books-by-title")
    public ResponseEntity<Object> handleGetBooksByTitle(@RequestBody LinkedHashMap<String, String> body) {
        try {
            String title = body.get("title");
            return new ResponseEntity<>(bs.getBookByTitle(title), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/book/get-books-by-author")
    public ResponseEntity<Object> handleGetBooksByAuthor(@RequestBody LinkedHashMap<String, String> body) {
        try {
            String author = body.get("author");
            return new ResponseEntity<>(bs.getBookByAuthor(author), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/book/get-books-by-isbn")
    public ResponseEntity<Object> handleGetBooksByIsbn(@RequestBody LinkedHashMap<String, String> body) {
        try {
            long isbn = Long.parseLong(body.get("isbn"));
            return new ResponseEntity<>(bs.getBookByIsbn(isbn), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/book/get-books-by-genreId")
    public ResponseEntity<Object> handleGetBooksByGenreId(@RequestBody LinkedHashMap<String, String> body) {
        try {
            int genreId = Integer.parseInt(body.get("genreId"));
            return new ResponseEntity<>(bs.getBookByGenreId(genreId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/remove-books-by-isbn")
    public ResponseEntity<Object> handleDeleteBooksByIsbn(@RequestBody LinkedHashMap<String, String> body) {
        try {
            long isbn = Long.parseLong(body.get("isbn"));
            bs.deleteBookByIsbn(isbn);
            String message = "Book with isbn " + isbn + " has been delete";
            return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not find this book", HttpStatus.NOT_FOUND);
        }
    }



}
