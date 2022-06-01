package com.revature.services;



import com.revature.exceptions.ExistingUserException;
import com.revature.exceptions.NullBookException;
import com.revature.exceptions.NullUserException;
import com.revature.models.Book;
import com.revature.models.User;
import com.revature.repo.UserRepo;
import com.revature.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {
    private UserRepo ur;
    @Autowired
    private EmailUtil emailUtil;
    private BookService bs;

    @Autowired
    public UserService(UserRepo ur, BookService bs){
        this.ur = ur;
        this.bs = bs;
    }

    /**
     * Registers a new user
     * @param email user's email
     * @param password user's password
     * @param first user's first name
     * @param last user's last name
     * @return returns the new user registered
     */
    public User registerNewUser(String email, String password, String first, String last) throws ExistingUserException {
        if (ur.findUserByEmail(email) != null) {
            throw new ExistingUserException("This email is already taken");
        }
        User register = new User(email, password, first, last);
        String body = emailUtil.generateWelcomeEmail(first, last);
        emailUtil.sendEmail(email, "Welcome to the Virtual Public Library!", body);
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

    /**
     * Gets all users
     * @return A list of all users
     */
    public List<User> getAllUsers(){
        return ur.findAll();
    }

    /**
     *
     * @param userId
     * @param isbn
     * @throws NullBookException
     */
    public void checkOutBook(int userId, long isbn) throws NullBookException {
        Book currentBook = bs.getBookByIsbn(isbn);
        User user = ur.findById(userId).get();
        user.addCheckOut(currentBook);
        ur.save(user);
        bs.checkOutBook(isbn);
    }

    public List<Book> getCheckedOutBooks(int userId){
        return ur.findById(userId).get().getCheckedOut();
    }

}
