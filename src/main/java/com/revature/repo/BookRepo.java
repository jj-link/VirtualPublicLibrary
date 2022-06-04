package com.revature.repo;

import com.revature.models.Book;
import com.revature.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    Book findBookByIsbn(long isbn);

    Book findBookByTitle(String title);

    List<Book> findAllBooksByAuthor(String author);

    List<Book> findAllByGenreId(int genreId);

    @Query(value = "SELECT * FROM books WHERE author LIKE %?1% or title LIKE %?1% or summary LIKE %?1% " +
                    "or cast(year_published as varchar(5)) LIKE %?1% or cast(isbn as varchar(14)) Like %?1%",
        nativeQuery = true)
    List<Book> searchBooks(String input);
}
