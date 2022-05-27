package com.revature.services;



import com.revature.exceptions.NullUserException;
import com.revature.models.User;
import com.revature.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private UserRepo ur;

    @Autowired
    public UserService(UserRepo ur){
        this.ur = ur;
    }

    /**
     *
     * @param email user's email
     * @param password user's password
     * @param first user's first name
     * @param last user's last name
     * @return returns the new user registered
     */
    public User registerNewUser(String email, String password, String first, String last){
        User register = new User(email, password, first, last);
        return ur.save(register);
    }

    /**
     *
     * @param email user's email
     * @param password user's password
     * @return email and password of the user logged in
     */

    public User loginUser(String email, String password) throws NullUserException {
        User login = ur.findUserByEmailAndPassword(email, password);
        if(login == null){
            throw new NullUserException();
        }
        return login;
    }
}
