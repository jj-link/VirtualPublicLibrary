package com.revature.controllers;


import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.utils.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@CrossOrigin("*")
public class UserController {

    private UserService us;

    @Autowired
    public UserController(UserService us){
        this.us = us;
    }

    @PostMapping("/user/register")
    public ResponseEntity<Object> handleRegisterUser(@RequestBody LinkedHashMap<String, String> body){
        try{
            User u = us.registerNewUser(body.get("email"), body.get("password"), body.get("firstName"), body.get("lastName"));
            LoggingUtil.logger.info("Successfully registered with email " + body.get("email"));
            return new ResponseEntity<>(u, HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
            LoggingUtil.logger.info("Could not register user");
            return new ResponseEntity<>("Email already taken.", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<Object> handleLoginUser(@RequestBody LinkedHashMap<String, String> body){
        String email = body.get("email");
        String password = body.get("password");
        try{
            LoggingUtil.logger.info("Successfully logged in user " + email);
            return new ResponseEntity<>(us.loginUser(email, password), HttpStatus.ACCEPTED);
        }catch(Exception e){
            LoggingUtil.logger.info("Could not log in user " + email);
            return new ResponseEntity<>("Invalid email or password", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/user/edit")
    public ResponseEntity<Object> handleUpdateProfile(@RequestBody LinkedHashMap<String, String> body){
        int id = Integer.parseInt(body.get("userId"));
        String email = body.get("email");
        String password = body.get("password");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        try{
            LoggingUtil.logger.info("Successfully edited user " + email);
            return new ResponseEntity<>(us.updateUser(id, email, password, firstName, lastName), HttpStatus.ACCEPTED);
        }catch(Exception e){
            e.printStackTrace();
            LoggingUtil.logger.info("Could not edit user " + email);
            return new ResponseEntity<>("Invalid email or password", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/user/view-user")
    public ResponseEntity<Object> handleFindUserById(@RequestBody LinkedHashMap<String, String> body) {
        int id = Integer.parseInt(body.get("userId"));
        try {
            LoggingUtil.logger.info("Successfully viewed user with ID# " + id);
            return new ResponseEntity<>(us.findUserById(id), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LoggingUtil.logger.info("Could not view user with ID# " + id);
            return new ResponseEntity<>("Invalid user id", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/user/all-users")
    public ResponseEntity<Object> handleGetAllUsers() {
        try {
            LoggingUtil.logger.info("Successfully viewed all users");
            return new ResponseEntity<>(us.getAllUsers(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            LoggingUtil.logger.info("Could not view all users");
            return new ResponseEntity<>("Could not get all users", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/user/checkout-show/{userId}")
    public ResponseEntity<Object> handleCheckOutShow(@PathVariable("userId") int userId){
        try {
            LoggingUtil.logger.info("Successfully showed checked out books from user with ID# " + userId);
            return new ResponseEntity<>(us.getCheckedOutBooks(userId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("Could not show checked out books from user with ID# " + userId);
            return new ResponseEntity<>("Could not find user", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/user/checkout-book")
    public ResponseEntity<Object> handleCheckOutAdd(@RequestBody LinkedHashMap<String, String> body){
        try {
            int userId = Integer.parseInt(body.get("userId"));
            long isbn = Long.parseLong(body.get("isbn"));
            us.checkOutBook(userId, isbn);
            String message = "Book has been checked out";
            LoggingUtil.logger.info("User with ID# " + userId + " successfully checked out book with ISBN " + isbn);
            return new ResponseEntity<>(us.getCheckedOutBooks(userId), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logger.info("User with ID# " + Integer.parseInt(body.get("userId")) + " could not check out book with ISBN " + Long.parseLong(body.get("isbn")));
            return new ResponseEntity<>("Could not check out book", HttpStatus.CONFLICT);
        }
    }

}