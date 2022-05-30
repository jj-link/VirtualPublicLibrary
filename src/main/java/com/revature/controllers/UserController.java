package com.revature.controllers;


import com.revature.models.User;
import com.revature.services.UserService;
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
            return new ResponseEntity<>(u, HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Email already taken.", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<Object> handleLoginUser(@RequestBody LinkedHashMap<String, String> body){
        String email = body.get("email");
        String password = body.get("password");
        try{
            return new ResponseEntity<>(us.loginUser(email, password), HttpStatus.ACCEPTED);
        }catch(Exception e){
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
            System.out.println("hello world");
            return new ResponseEntity<>(us.updateUser(id, email, password, firstName, lastName), HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity<>("Invalid email or password", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}