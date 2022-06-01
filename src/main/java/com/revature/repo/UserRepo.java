package com.revature.repo;

import com.revature.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findUserByEmailAndPassword(String email, String password);

    User findUserByEmail(String email);

}
