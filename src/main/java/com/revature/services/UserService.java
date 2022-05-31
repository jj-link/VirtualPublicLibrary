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
     * Registers a new user
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
     * Logs in a user
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

    /**
     * Updates a user's info
     * @param id The id of the user to update
     * @param email The user's new email
     * @param password The user's new password
     * @param first The user's new first name
     * @param last The user's new last name
     * @return The User with the updated information
     */
    public User updateUser(int id, String email, String password, String first, String last) {
        User current = ur.findById(id).get();

        current.setEmail(email);
        current.setPassword(password);
        current.setFirstName(first);
        current.setLastName(last);

        ur.save(current);

        return current;
    }

    /**
     * Finds a user by their id
     * @param id The id of the user to find
     * @return The specified user
     */
    public User findUserById(int id){
        User getUser = ur.findById(id).get();
        return getUser;
    }

    // delete


    // get all users

}
