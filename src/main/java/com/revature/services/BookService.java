package com.revature.services;

import com.revature.exceptions.ExistingBookException;
import com.revature.exceptions.ExistingUserException;
import com.revature.exceptions.NullBookException;
import com.revature.exceptions.NullUserException;
import com.revature.models.Book;
import com.revature.models.User;
import com.revature.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookService {

    private BookRepo br;

    @Autowired
    public BookService(BookRepo br){
        this.br = br;
    }

    // create

    /**
     * Create a book
     * @param title Book title
     * @param author Book author
     * @param genreId Book genre
     * @param summary Book summary
     * @param isbn Book ISBN
     * @param yearPublished Year the book was published
     * @return The newly created book
     * @throws ExistingBookException Cannot create duplicates of a book
     */
    public Book createBook(String title, String author, int genreId, String summary, long isbn, int yearPublished)
        throws ExistingBookException{
        if (br.findBookByIsbn(isbn) != null) {
            throw new ExistingBookException("This book is already exist.");
        }
        Book result = new Book(title, author, genreId, summary, isbn, yearPublished);
        return br.save(result);
    }

    // update


    // get all books
    public List<Book> getAllBooks() {
        return br.findAll();
    }

    // get book by title

    public Book getBookByTitle(String title) throws NullBookException {
        Book getBook = br.findBookByTitle(title);
        if(getBook == null){
            throw new NullBookException();
        }
        return getBook;
    }
    // get book by author

    public List<Book> getBookByAuthor(String author) throws NullBookException {
        List <Book> bookList = new ArrayList<>();
        bookList = br.findAllBookByAuthor(author);
        if(bookList.isEmpty()){
            throw new NullBookException();
        }
        return bookList;
    }

    // get list of book by genreId
    public List<Book> getBookByGenreId(int genreId){
        return br.findAllByGenreId(genreId);
    }
    // get book by isbn
    public Book getBookByIsbn(long isbn) throws NullBookException {
        Book getBook = br.findBookByIsbn(isbn);
        if(getBook == null){
            throw new NullBookException();
        }
        return getBook;
    }



    // get all of a user's checked out books

    // delete
    public void deleteBookByIsbn(long isbn) throws NullBookException {
        Book getBook = br.findBookByIsbn(isbn);
        if(getBook == null){
            throw new NullBookException();
        }
        br.delete(getBook);
    }

}
