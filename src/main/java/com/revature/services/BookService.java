package com.revature.services;

import com.revature.exceptions.ExistingBookException;
import com.revature.exceptions.NullBookException;
import com.revature.models.Book;
import com.revature.repo.BookRepo;
import com.revature.utils.BookCheckedOutComparator;
import com.revature.utils.BookIdComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BookService {

    private BookRepo br;

    @Autowired
    public BookService(BookRepo br){
        this.br = br;
    }

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
            throw new ExistingBookException("This book already exists.");
        }
        Book result = new Book(title, author, genreId, summary, isbn, yearPublished);
        return br.save(result);
    }

    /**
     * Updates an existing book
     * @param id The id of the book
     * @param title The book's title
     * @param author The book's author
     * @param genreId The book's genre
     * @param summary The book's summary
     * @param isbn The book's ISBN
     * @param year The year the book was published
     * @return The newly updated book
     */
    public Book updateBook(int id, String title, String author, int genreId, String summary, long isbn, int year) {
        Book book = br.findById(id).get();

        if (!title.equals("")) book.setTitle(title);
        if (!author.equals("")) book.setAuthor(author);
        if (genreId != 0) book.setGenreId(genreId);
        if (!summary.equals("")) book.setSummary(summary);
        if (isbn != 0) book.setIsbn(isbn);
        if (year != 0) book.setYearPublished(year);

        return br.save(book);
    }

    /**
     * Get all books
     * @return A list of all books
     */
    public List<Book> getAllBooks() {
        return br.findAll();
    }

    /**
     * Gets a book by its title
     * @param title The book's title
     * @return The book with the correct title
     * @throws NullBookException Cannot find book
     */
    public Book getBookByTitle(String title) throws NullBookException {
        Book getBook = br.findBookByTitle(title);
        if(getBook == null){
            throw new NullBookException();
        }
        return getBook;
    }

    /**
     * Gets all books from an author
     * @param author The author to find books from
     * @return All te books by the specified author
     * @throws NullBookException The book list might be empty
     */
    public List<Book> getBookByAuthor(String author) throws NullBookException {
        List <Book> bookList = new ArrayList<>();
        bookList = br.findAllBooksByAuthor(author);
        if(bookList.isEmpty()){
            throw new NullBookException();
        }
        return bookList;
    }

    /**
     * Get all books from a specific genre
     * @param genreId The genre id
     * @return All books of a specific genre
     */
    public List<Book> getBookByGenreId(int genreId){
        return br.findAllByGenreId(genreId);
    }

    /**
     * Gets a book by its ISBN
     * @param isbn The book's ISBN
     * @return A book with the specified ISBN
     * @throws NullBookException The book may not exist
     */
    public Book getBookByIsbn(long isbn) throws NullBookException {
        Book getBook = br.findBookByIsbn(isbn);
        if(getBook == null){
            throw new NullBookException();
        }
        return getBook;
    }

    /**
     * Deletes a book by its ISBN
     * @param isbn The book's ISBN
     * @throws NullBookException Cannot delete a nonexistent book
     */
    public void deleteBookByIsbn(long isbn) throws NullBookException {
        Book getBook = br.findBookByIsbn(isbn);
        if(getBook == null){
            throw new NullBookException();
        }
        br.delete(getBook);
    }

    /**
     * Gets the top 10 most popular books
     * @return A list of the most popular books
     * @throws NullBookException
     */
    public List<Book> getMostPopularBooks() {
        List<Book> result = new ArrayList<>();
        List<Book> allBooks = br.findAll();
        Collections.sort(allBooks, new BookCheckedOutComparator());
        Collections.reverse(allBooks);
        for (int i = 0; i < 10; i++) {
            if (i == allBooks.size()) break;
            result.add(allBooks.get(i));
        }
        return result;
    }

    /**
     * Allows a user to check out a book
     * @param isbn The ISBN of the book to check out
     * @throws NullBookException The book might not exist
     */
    public void checkOutBook(long isbn) throws NullBookException{
        Book book = getBookByIsbn(isbn);
        int currentBookCheckOut = book.getCheckedOutCount() + 1;
        book.setCheckedOutCount(currentBookCheckOut);
        br.save(book);
    }

    /**
     * Gets the most recent books added
     * @return A list of the top 10 recently added books
     */
    public List<Book> getRecentBooks() {
        List<Book> result = new ArrayList<>();
        List<Book> allBooks = br.findAll();
        Collections.sort(allBooks, new BookIdComparator());
        Collections.reverse(allBooks);
        for (int i = 0; i < 10; i++) {
            if (i == allBooks.size()) break;
            result.add(allBooks.get(i));
        }
        return result;
    }

    public List<Book> searchBooks(String input) {
        return br.searchBooks(input);
    }

}
