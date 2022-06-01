package com.revature.repo;

import com.revature.models.Book;
import com.revature.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    Book findBookByIsbn(long isbn);

    Book findBookByTitle(String title);

}
