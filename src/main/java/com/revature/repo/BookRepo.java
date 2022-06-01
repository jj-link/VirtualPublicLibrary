package com.revature.repo;

import com.revature.models.Book;
import com.revature.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    Book findBookByIsbn(long isbn);

    Book findBookByTitle(String title);

    List<Book> findAllBookByAuthor(String author);

    List<Book> findAllByGenreId(int genreId);
}
